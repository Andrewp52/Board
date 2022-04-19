package com.pashenko.Board.entities;

public interface DtoConvertible<T>{
    T getShortDto();
    T getFullDto();
}
