package dlx.client.model;

import java.io.Serializable;
import java.util.List;

public class ListResponse<T> extends Response implements Serializable {
    
    public List<T> items;
    
    
}
