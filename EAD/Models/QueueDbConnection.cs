namespace EAD.Models
{
    public class QueueDbConnection
    {
        public string ConnectionString { get; set; } = null!;

        public string DatabaseName { get; set; } = null!;

        public string QueueCollectionName { get; set; } = null!;
    }
}
