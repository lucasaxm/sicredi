# Requisitos
- Java 11
- MongoDB 5.0.2

> Caso queira rodar o MongoDB em um container docker, use o seguinte comando
> 
> `docker run --name mongo-sicredi -p 27017:27017 -v <caminho-raiz>/data/db:/data/db -d mongo:5.0.2`
> 
> Substitua o `<caminho-raiz>` pelo caminho absoluto da raiz da aplicação

# Como iniciar a aplicação localmente
 
##### Para buildar a aplicação execute o seguinte comando:
- Unix: `./gradlew clean build`
- Windows: `gradlew.bat clean build`

##### Para iniciar a aplicação execute o seguinte comando:
- Unix: `./gradlew bootRun --args='--spring.profiles.active=dev --server.port=8090'`
- Windows: `gradlew.bat bootRun --args='--spring.profiles.active=dev --server.port=8090'`

##### Para rodar os testes execute o seguinte comando:
- Unix: `./gradlew clean test`
- Windows: `gradlew.bat clean test`

> Os resultados dos testes estarão disponíveis em `<caminho-raiz>/build/reports/tests/test/index.html`

##### Os endpoints base da API são
- [http://localhost:8090/associados](http://localhost:8080/associados)
- [http://localhost:8090/pautas](http://localhost:8080/pautas)


##### Para mais informações sobre como utilizar a API acesse a documentação através do Swagger:

- Swagger UI: http://localhost:8090/swagger-ui.html
- Swagger API Docs: http://localhost:8090/v2/api-docs
