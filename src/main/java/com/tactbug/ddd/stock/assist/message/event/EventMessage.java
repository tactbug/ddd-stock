package com.tactbug.ddd.stock.assist.message.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventMessage<T, E> {
    private Object aggregateId;
    private T eventType;
    private E event;
}
