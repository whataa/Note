package io.github.whataa.picer;

import java.util.List;

public interface EventCallback {
    int EVENT_COMPLETE = 0x01;
    int EVENT_CANCEL = 0x02;
    int EVENT_CAMERA = 0x03;

    /**
     * when the event is EVENT_COMPLETE, the pics is not null or empty.
     */
   void onEvent(int type, List<String> pics);
}