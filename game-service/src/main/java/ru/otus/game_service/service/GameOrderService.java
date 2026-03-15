package ru.otus.game_service.service;

import ru.otus.game_service.model.Order;

import java.util.Map;

public interface GameOrderService {

    Map<String, Object> orderAction(String userId, String gameId, Order order);

}
