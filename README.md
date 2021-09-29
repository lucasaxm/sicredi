# Requisitos
- Java 11
- MongoDB 5.0.2

# Como iniciar a aplicação localmente

##### Antes de começar:
- substitua as seguintes propriedades do mongoDB no arquivo `application-dev.properties`:
```
spring.data.mongodb.host=<mongo_host>
spring.data.mongodb.port=<mongo_port>
```

##### Para buildar a aplicação execute o seguinte comando:
- Unix: `./gradlew clean build -Dspring.profiles.active=dev`
- Windows: `gradlew.bat clean build -Dspring.profiles.active=dev`

##### Para iniciar a aplicação execute o seguinte comando:
- Unix: `./gradlew bootRun -Dspring.profiles.active=dev`
- Windows: `gradlew.bat bootRun -Dspring.profiles.active=dev`

##### Para rodar os testes execute o seguinte comando:
- Unix: `./gradlew clean test`
- Windows: `gradlew.bat clean test`

    Test reports are available in `build/reports/tests/test/index.html` on your file system

##### Os endpoints base da API são
- [http://localhost:8080/associados](http://localhost:8080/associados)
- [http://localhost:8080/pautas](http://localhost:8080/pautas)


##### Para mais informações sobre como utilizar a API acesse a documentação através do Swagger:

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Swagger API Docs: [http://localhost:8080/v2/api-docs](http://localhost:8080/v2/api-docs)