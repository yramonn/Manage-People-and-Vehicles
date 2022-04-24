package com.sovos.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sovos.model.dto.PessoaDTO;
import com.sovos.model.response.Pessoa;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PessoaService {

    private final int TIMEOUT_CONNECTION = 30;
    private final int TIMEOUT_SOCKET_CONNECTION = 60;

    private PessoaDTO pessoaDTO;

    public PessoaService(PessoaDTO pessoaDTO) {

        this.pessoaDTO = pessoaDTO;

    }

    public static void main(String[] args) {
        PessoaService pessoaService = new PessoaService(new PessoaDTO());
        List<Pessoa> pessoas = pessoaService.getAllPessoas();
        System.out.println(Arrays.asList(pessoas));
    }

    public List<Pessoa> getAllPessoas() {

        CloseableHttpClient client;
        String result = "";

        try {
            client = HttpClientBuilder.create()
                    .useSystemProperties()
                    .setDefaultRequestConfig(getRequestConfiguration())
                    .setConnectionManager(new BasicHttpClientConnectionManager())
                    .build();

            StringEntity entity = new StringEntity("", StandardCharsets.UTF_8);
            entity.setChunked(true);

            HttpGet httpGet = new HttpGet("http://localhost:8080/pessoa");
            httpGet.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
            httpGet.addHeader(HTTP.CHUNK_CODING, "true");

            HttpResponse response = client.execute(httpGet);

            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("Url da requisicao: " + httpGet.getURI());
            System.out.println("Codigo da resposta: " + responseCode);

            if (responseCode == 200 || responseCode == 204) {
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
                    System.out.println("Resposta recebida da API:");
                    System.out.println(result);
                }
            }else if(responseCode == 404){
                System.out.println("Nao ha dados para serem processados");
            }

            client.close();

            if(!result.isEmpty()){
                return httpResponseToPessoas(result);
            }

        } catch (ClientProtocolException e) {
            System.err.println("Error client protocol exception : " + e);
        } catch (IOException e) {
            System.err.println("Error IOException : " + e);
        }

        return Collections.emptyList();
    }

    private RequestConfig getRequestConfiguration() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_CONNECTION * 1000)
                .setConnectionRequestTimeout(TIMEOUT_CONNECTION * 1000)
                .setSocketTimeout(TIMEOUT_SOCKET_CONNECTION * 1000)
                .build();
        return config;
    }

    private String post(String json){
        String result = "";
        CloseableHttpClient client;

        try{

            client = HttpClientBuilder.create()
                    .useSystemProperties()
                    .setDefaultRequestConfig(getRequestConfiguration())
                    .setConnectionManager(new BasicHttpClientConnectionManager())
                    .build();

            StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
            entity.setChunked(true);

            HttpPost httpPost = new HttpPost("http://localhost:8080/pessoa");
            httpPost.setEntity(entity);
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
            httpPost.addHeader(HTTP.CHUNK_CODING, "true");

            HttpResponse response = client.execute(httpPost);

            int responseCode = response.getStatusLine().getStatusCode();

            if(responseCode == 200 || responseCode == 204) {
                HttpEntity httpEntity = response.getEntity();
                if(httpEntity != null){
                    result = EntityUtils.toString(httpEntity,StandardCharsets.UTF_8);
                }
            }

            client.close();

        }catch(Exception ex){
            System.err.println("Erro na requisicao : " + ex);
        }

        return result;
    }

    private String put(String json){
        String result = "";
        CloseableHttpClient client;

        try{

            client = HttpClientBuilder.create()
                    .useSystemProperties()
                    .setDefaultRequestConfig(getRequestConfiguration())
                    .setConnectionManager(new BasicHttpClientConnectionManager())
                    .build();

            StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
            entity.setChunked(true);

            HttpPut http = new HttpPut("http://localhost:8080/pessoa/" + pessoaDTO.getId());
            http.setEntity(entity);
            http.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
            http.addHeader(HTTP.CHUNK_CODING, "true");

            HttpResponse response = client.execute(http);

            int responseCode = response.getStatusLine().getStatusCode();

            if(responseCode == 200 || responseCode == 204) {
                HttpEntity httpEntity = response.getEntity();
                if(httpEntity != null){
                    result = EntityUtils.toString(httpEntity,StandardCharsets.UTF_8);
                }
            }

            client.close();

        }catch(Exception ex){
            System.err.println("Erro na requisicao : " + ex);
        }

        return result;
    }

    private List<Pessoa> httpResponseToPessoas(String resp) {
        Gson gson = new Gson();
        Type type = new TypeToken<Collection<Pessoa>>(){}.getType();
        return gson.fromJson(resp, type);
    }

    public Pessoa savePessoa() {

        Pessoa pessoa = pessoaDTOToPessoa();
        Gson gson = new Gson();
        String json = gson.toJson(pessoa);
        String resultado = post(json);
        System.out.println("Resultado da gravacao : " + resultado);
        pessoa = gson.fromJson(resultado,Pessoa.class);
        return pessoa;
    }

    private Pessoa pessoaDTOToPessoa() {

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaDTO.getName());
        pessoa.setEndereco(pessoaDTO.getAddress());
        pessoa.setCpf(pessoaDTO.getCpf());

        return pessoa;
    }

    public Pessoa alterPessoa() {

        Pessoa pessoa = pessoaDTOToPessoa();
        Gson gson = new Gson();
        String json = gson.toJson(pessoa);
        String resultado = put(json);
        System.out.println("Resultado da alteracao : " + resultado);
        pessoa = gson.fromJson(resultado,Pessoa.class);
        return pessoa;

    }

    public Pessoa pessoaById() {
        CloseableHttpClient client;
        String result = "";

        try {
            client = HttpClientBuilder.create()
                    .useSystemProperties()
                    .setDefaultRequestConfig(getRequestConfiguration())
                    .setConnectionManager(new BasicHttpClientConnectionManager())
                    .build();

            HttpGet httpGet = new HttpGet("http://localhost:8080/pessoa/"+pessoaDTO.getId());
            httpGet.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
            httpGet.addHeader(HTTP.CHUNK_CODING, "true");

            HttpResponse response = client.execute(httpGet);

            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("Url da requisicao: " + httpGet.getURI());
            System.out.println("Codigo da resposta: " + responseCode);

            if (responseCode == 200 || responseCode == 204) {
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
                    System.out.println("Resposta recebida da API:");
                    System.out.println(result);
                }
            }else if(responseCode == 404){
                System.out.println("Nao ha dados para serem processados");
            }

            client.close();

            if(!result.isEmpty()){
                return httpResponseToPessoa(result);
            }

        } catch (ClientProtocolException e) {
            System.err.println("Error client protocol exception : " + e);
        } catch (IOException e) {
            System.err.println("Error IOException : " + e);
        }

        return new Pessoa();
    }

    public boolean delete(){
        CloseableHttpClient client;

        try {
            client = HttpClientBuilder.create()
                    .useSystemProperties()
                    .setDefaultRequestConfig(getRequestConfiguration())
                    .setConnectionManager(new BasicHttpClientConnectionManager())
                    .build();

            HttpDelete http = new HttpDelete("http://localhost:8080/pessoa/" + pessoaDTO.getId());
            http.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
            http.addHeader(HTTP.CHUNK_CODING, "true");

            HttpResponse response = client.execute(http);

            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("Url da requisicao: " + http.getURI());
            System.out.println("Codigo da resposta: " + responseCode);

            if (responseCode == 200 || responseCode == 204) {
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    System.out.println("Resposta recebida da API:");
                    System.out.println(EntityUtils.toString(httpEntity, StandardCharsets.UTF_8));
                    client.close();
                    return true;
                }
            }else if(responseCode == 404){
                System.out.println("Nao ha dados para serem processados");
            }

            client.close();
        } catch (ClientProtocolException e) {
            System.err.println("Error client protocol exception : " + e);
        } catch (IOException e) {
            System.err.println("Error IOException : " + e);
        }

        return false;
    }


    private Pessoa httpResponseToPessoa(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, Pessoa.class);
    }

}
