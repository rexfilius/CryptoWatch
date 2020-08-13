package com.ifyosakwe.data.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifyosakwe.data.entities.CryptoCoinEntity;
import com.ifyosakwe.data.models.CoinModel;

import java.util.ArrayList;
import java.util.List;

public class CryptoMapper extends ObjectMapper {

    // TODO: "https://files.coinmarketcap.com/static/img/coins/128x128/%s.png"
    // confirm the URL above for CRYPTO_URL_PATH
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

    public List<CoinModel> mapEntityToModel(List<CryptoCoinEntity> data) {
        final ArrayList<CoinModel> listData = new ArrayList<>();
        CryptoCoinEntity entity;

        for (int i = 0; i < data.size(); i++) {
            entity = data.get(i);
            listData.add(new CoinModel(entity.getName(), entity.getSymbol(),
                    String.format(CRYPTO_URL_PATH, entity.getId()), entity.getPriceUsd(),
                    entity.get_24hVolumeUsd(), Double.valueOf(entity.getMarketCapUsd())));
        }

        return listData;
    }

    public String mapEntitiesToString(List<CryptoCoinEntity> data)
            throws JsonProcessingException {
        return writeValueAsString(data);
    }
}
