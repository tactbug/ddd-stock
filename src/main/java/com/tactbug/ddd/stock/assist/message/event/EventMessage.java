package com.tactbug.ddd.stock.assist.message.event;

import com.tactbug.ddd.stock.assist.utils.CodeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventMessage<T, E> {

    private Long messageId;
    private Object aggregateId;
    private T eventType;
    private E event;

    public EventMessage(Object aggregateId, T eventType, E event){
        messageId = CodeUtil.nextId();
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.event = event;
    }

}
