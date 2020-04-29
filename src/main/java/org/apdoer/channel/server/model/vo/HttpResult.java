package org.apdoer.channel.server.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author apdoer
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpResult {
    private String statusCode;

    private String content;
}