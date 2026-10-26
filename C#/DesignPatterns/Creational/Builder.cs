

namespace Project.DesignPatterns.Creational;

/// <summary>
/// Connection string builder
/// </summary>
/// <see cref="SqlConnectionString"/>
public class ConnectionStringBuilder
{
    public string? DataSource { get; set; }
    public string? InitialCatalog { get; set; }
    public string? UserID { get; set; }
    public string? Password { get; set; }
    public bool? Encrypt { get; set; }

    public override string ToString()
    {
        var result = new List<string>();

        Pairing(result, GetType().GetProperties());
        
        return string.Join(";", result);
    }

    private void Pairing(IList<string> result, params System.Reflection.PropertyInfo[] propertyInfos)
    {
        foreach (var propertyInfo in propertyInfos)
        {
            var value = propertyInfo.GetValue(this);

            if (value != null)
            {
                result.Add($"{propertyInfo.Name}='{value}'");
            }
        }

    }
}

