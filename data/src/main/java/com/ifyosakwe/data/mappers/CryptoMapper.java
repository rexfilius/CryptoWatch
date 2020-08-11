package com.ifyosakwe.data.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifyosakwe.data.entities.CryptoCoinEntity;

import java.util.ArrayList;

public class CryptoMapper extends ObjectMapper {

    private final String CRYPTO_URL_PATH = "";

    public ArrayList<CryptoCoinEntity> mapJsonToEntity(String jsonStr) {
        ArrayList<CryptoCoinEntity> data = null;
        try {
            data = readValue(jsonStr, new TypeReference<ArrayList<CryptoCoinEntity>>() {
            });
        } catch (Exception e) {
        }
        return data;
    }
}
