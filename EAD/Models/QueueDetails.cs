using MongoDB.Bson;

namespace EAD.Models
{
    public class QueueDetails
    {
        public ObjectId Id { get; set; }
        public int QueueId { get; set; }
        public string VehicleType { get; set; } = "";
        public string FuelType { get; set; } = "";
        public string Status { get; set; } = "";
        public int FuelStationId { get; set; }
        public int UserId { get; set; }
    }
}
