using EAD.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace EAD.Services
{
    public class UsersService
    {
        private readonly IMongoCollection<FuelDetails> _Collection;
        private readonly IMongoCollection<Users> _UserCollection;
        public UsersService(IOptions<UserDbConnection> userDatabaseSettings)
        {
            var mongoClient = new MongoClient(
                userDatabaseSettings.Value.ConnectionString);

            var mongoDatabase = mongoClient.GetDatabase(
                userDatabaseSettings.Value.DatabaseName);

            _UserCollection = mongoDatabase.GetCollection<Users>(
                userDatabaseSettings.Value.UserCollectionName); ;
        }

        public JsonResult Get()
        {
            var datalist = _UserCollection.AsQueryable();
            return new JsonResult(datalist);
        }

        public JsonResult Post(Users users)
        {
            int lastUserId = _UserCollection.AsQueryable().Count();
            users.UserId = lastUserId + 1;
            _UserCollection.InsertOne(users);
            return new JsonResult("Adding Successfull");
        }

        public JsonResult Put(Users users)
        {
            var filter = Builders<Users>.Filter.Eq("UserId", users.UserId);
            var update = Builders<Users>.Update.Set("UserName", users.UserName).Set("Password", users.Password)
                .Set("Role", users.Role);
            _UserCollection.UpdateOne(filter, update);
            return new JsonResult("Update Successfull");
        }

        public JsonResult Delete(int id)
        {
            var filter = Builders<Users>.Filter.Eq("UserId", id);
            _UserCollection.DeleteOne(filter);
            return new JsonResult("Delete Successfull");
        }

        public JsonResult Queue(string UserName, string Password)
        {
            var filter = Builders<Users>.Filter.Eq("UserName", UserName) & Builders<Users>.Filter.Eq("Password", Password);
            var datalist = _UserCollection.Find(filter).ToList().First();
            return new JsonResult(datalist);
        }
    }
}
