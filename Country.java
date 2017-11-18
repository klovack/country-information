package gui.country.combo;

public class Country
{
    private String name;

    private String capital;

    private long people;

    private long area;

    public Country(String name, String capital, long people, long area)
    {
        super();
        this.name = name;
        this.capital = capital;
        this.people = people;
        this.area = area;
    }

    public String getName()
    {
        return name;
    }

    public String getCapital()
    {
        return capital;
    }

    public long getPeople()
    {
        return people;
    }

    public long getArea()
    {
        return area;
    }

    public long getDensity()
    {
        return (Math.round((double) people / (double) area));
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
