package com.tactbug.ddd.stock.aggregate.function;

import com.tactbug.ddd.stock.aggregate.Warehouse;

@FunctionalInterface
public interface IterateChildren {
    void accept(Warehouse parent, Warehouse target);
}
