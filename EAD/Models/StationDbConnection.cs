namespace EAD.Models
{
    public class StationDbConnection
    {
        public string ConnectionString { get; set; } = null!;

        public string DatabaseName { get; set; } = null!;

        public string StationCollectionName { get; set; } = null!;
    }
}
