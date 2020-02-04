package cloud.auction.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product<T> {
    private String id;
    private String name;
    private String price;
    private String description;
    private String active;
//    private Category category;
    private List<Image> images;
    private T bids;

}
