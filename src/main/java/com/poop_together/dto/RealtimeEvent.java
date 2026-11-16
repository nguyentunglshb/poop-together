package com.poop_together.dto;

import lombok.Builder;

@Builder
public record RealtimeEvent(String type, Object payload) {

}
