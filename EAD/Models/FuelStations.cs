using MongoDB.Bson;

namespace EAD.Models
{
    public class FuelStations
    {
        public ObjectId Id { get; set; }
        public int FuelStationId { get; set; }
        public string FuelStationName { get; set; } = "";
        public String Location { get; set; } = "";
        public String Opentime { get; set; } = "";
        public String Closetime { get; set; } = "";
        public int UserId { get; set; }
    }
}
