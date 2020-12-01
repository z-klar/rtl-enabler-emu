package cz.zk.springmvcdemo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class globalData {

    public int [] dins = new int[2];
    public int [] douts = new int[16];


}
