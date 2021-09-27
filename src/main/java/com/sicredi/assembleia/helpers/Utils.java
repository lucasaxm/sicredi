package com.sicredi.assembleia.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.assembleia.errorhandling.exceptions.CpfUnableToVoteException;
import com.sicredi.assembleia.errorhandling.exceptions.InvalidCpfException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    private static volatile Utils instance;

    public static Utils getInstance() {
        Utils result = instance;
        if (result != null){
            return result;
        }
        synchronized(Utils.class) {
            if (instance == null) {
                instance = new Utils();
            }
            return instance;
        }
    }

    public boolean isNullOrEmpty(String input) {
        return (input == null || input.isBlank());
    }

    public static void cpfAbleToVote(String cpf) throws IOException, InvalidCpfException, CpfUnableToVoteException {
        URL url = new URL(String.format("https://user-info.herokuapp.com/users/%s", cpf));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        if (con.getResponseCode() != HttpStatus.OK.value()){
            throw new InvalidCpfException(cpf);
        }

        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(con.getInputStream());
        String response = node.get("status").textValue();

        if (!"ABLE_TO_VOTE".equalsIgnoreCase(response)){
            throw new CpfUnableToVoteException(cpf);
        }
    }
}
