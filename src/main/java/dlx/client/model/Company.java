package dlx.client.model;

import java.io.Serializable;

/**
 *
 * @author Joost
 */
public class Company implements Nameable, Identifiable, Serializable {

    public String companyId;
    public String catalogCompanyId;
    public String name;
    public String vatNumber;
    public String address;

}
