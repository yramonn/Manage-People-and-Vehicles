package com.sovos.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sovos.model.dto.VeiculosDTO;
import com.sovos.model.response.Veiculos;
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

public class VeiculosService {

    private final int TIMEOUT_CONNECTION = 30;
    private final int TIMEOUT_SOCKET_CONNECTION = 60;

    private VeiculosDTO veiculosDTO;

    public VeiculosService(VeiculosDTO veiculosDTO) {

        this.veiculosDTO = veiculosDTO;

    }

    public static void main(String[] args) {
        VeiculosService veiculosService = new VeiculosService(new VeiculosDTO());
        List<Veiculos> veiculos = veiculosService.getAllVeiculos();
        System.out.println(Arrays.asList(veiculos));
    }

    public List<Veiculos> getAllVeiculos() {

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

            HttpGet httpGet = new HttpGet("http://localhost:8181/veiculos");
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
                return httpResponseToVeiculos(result);
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

            HttpPost httpPost = new HttpPost("http://localhost:8181/veiculos");
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

            HttpPut http = new HttpPut("http://localhost:8181/veiculos/" + veiculosDTO.getId());
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

    private List<Veiculos> httpResponseToVeiculos(String resp) {
        Gson gson = new Gson();
        Type type = new TypeToken<Collection<Veiculos>>(){}.getType();
        return gson.fromJson(resp, type);
    }

    public Veiculos saveVeiculos() {

        Veiculos veiculos = veiculosDTOToVeiculos();
        Gson gson = new Gson();
        String json = gson.toJson(veiculos);
        String resultado = post(json);
        System.out.println("Resultado da gravacao : " + resultado);
        veiculos = gson.fromJson(resultado,Veiculos.class);
        return veiculos;
    }

    private Veiculos veiculosDTOToVeiculos() {

        Veiculos veiculos = new Veiculos();
        veiculos.setPlaca(veiculosDTO.getPlaca());
        veiculos.setCor(veiculosDTO.getCor());
        veiculos.setModelo(veiculosDTO.getModelo());
        veiculos.setCpfProprietario(veiculosDTO.getCpfProprietario());

        return veiculos;
    }

    public Veiculos alterVeiculos() {

        Veiculos veiculos = veiculosDTOToVeiculos();
        Gson gson = new Gson();
        String json = gson.toJson(veiculos);
        String resultado = put(json);
        System.out.println("Resultado da alteracao : " + resultado);
        veiculos = gson.fromJson(resultado,Veiculos.class);
        return veiculos;

    }

    public Veiculos veiculosById() {
        CloseableHttpClient client;
        String result = "";

        try {
            client = HttpClientBuilder.create()
                    .useSystemProperties()
                    .setDefaultRequestConfig(getRequestConfiguration())
                    .setConnectionManager(new BasicHttpClientConnectionManager())
                    .build();

            HttpGet httpGet = new HttpGet("http://localhost:8181/veiculos/"+veiculosDTO.getId());
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
                return httpResponseToVeiculo(result);
            }

        } catch (ClientProtocolException e) {
            System.err.println("Error client protocol exception : " + e);
        } catch (IOException e) {
            System.err.println("Error IOException : " + e);
        }

        return new Veiculos();
    }

    public boolean delete(){
        CloseableHttpClient client;

        try {
            client = HttpClientBuilder.create()
                    .useSystemProperties()
                    .setDefaultRequestConfig(getRequestConfiguration())
                    .setConnectionManager(new BasicHttpClientConnectionManager())
                    .build();

            HttpDelete http = new HttpDelete("http://localhost:8181/veiculos/" + veiculosDTO.getId());
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


    private Veiculos httpResponseToVeiculo(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, Veiculos.class);
    }

}
