package io.workshop.pruebas;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ContentLengthActivity {
    ContentLengthInfo count(String url);
}
