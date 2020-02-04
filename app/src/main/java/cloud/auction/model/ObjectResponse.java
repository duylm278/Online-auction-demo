package cloud.auction.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectResponse<T> implements Serializable {
    private boolean success;
    private  T data;
}
