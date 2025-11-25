package fr.imt.springforce.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Handler<T> {

    private Handler<T> first;
    private Handler<T> next;

    public Handler<T> link(Handler<T> next)
    {
        setNext(next);
        if (null == getFirst()) {
            setFirst(this);
        }
        getNext().setFirst(getFirst());
        return next;
    }

}