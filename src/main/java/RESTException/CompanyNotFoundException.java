package RESTException;

public class CompanyNotFoundException extends Exception
{
    public CompanyNotFoundException()
    {
    }

    public CompanyNotFoundException(String msg)
    {
        super(msg);
    }
}