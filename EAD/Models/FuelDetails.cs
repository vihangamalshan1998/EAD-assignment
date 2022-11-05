using MongoDB.Bson;

namespace EAD.Models
{
    public class FuelDetails
    {
        public ObjectId Id { get; set; }
        public int FuelDetailId{ get; set; }
        public string FuelType { get; set; } = "";
        public int Capacity { get; set; } = 0;
        public Boolean IsArrival { get; set; } = false;
        public int FuelStationId { get; set; }
    }
}
