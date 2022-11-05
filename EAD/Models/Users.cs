using MongoDB.Bson;

namespace EAD.Models
{
    public class Users
    {
        public ObjectId Id { get; set; }
        public int UserId { get; set; }
        public string UserName { get; set; } = "";
        public String Password { get; set; } = "";
        public String Role { get; set; } = "";
    }
}
