package com.nowcoder.async;

import java.util.List;

/**
 * @author plancer16
 * @create 2021/5/13 15:52
 */
public interface EventHandler {
    void doHandler(EventModel eventModel);
    List<EventType> getSupportEventTypes();
}
