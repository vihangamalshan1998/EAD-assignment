namespace EAD.Models
{
    public class FuelDbConnection
    {
        public string ConnectionString { get; set; } = null!;

        public string DatabaseName { get; set; } = null!;

        public string FuelCollectionName { get; set; } = null!;
    }
}
