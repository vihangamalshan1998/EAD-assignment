using EAD.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace EAD.Services
{
    public class FuelStationsService
    {
        private readonly IMongoCollection<FuelStations> _Collection;
        public FuelStationsService(IOptions<StationDbConnection> stationDatabaseSettings)
        {
            var mongoClient = new MongoClient(
                stationDatabaseSettings.Value.ConnectionString);

            var mongoDatabase = mongoClient.GetDatabase(
                stationDatabaseSettings.Value.DatabaseName);

            _Collection = mongoDatabase.GetCollection<FuelStations>(
                stationDatabaseSettings.Value.StationCollectionName);
        }

        public JsonResult Get()
        {
            var datalist = _Collection.Find(_ => true).ToList();
            return new JsonResult(datalist);
        }

        public JsonResult Post(FuelStations fuelStation)
        {
            int lastFuelStationId = _Collection.AsQueryable().Count();
            fuelStation.FuelStationId = lastFuelStationId + 1;
            _Collection.InsertOne(fuelStation);
            return new JsonResult("Adding Successfull");
        }

        public JsonResult Put(FuelStations fuelStation)
        {
            var filter = Builders<FuelStations>.Filter.Eq("FuelStationId", fuelStation.FuelStationId);
            var update = Builders<FuelStations>.Update.Set("FuelStationName", fuelStation.FuelStationName).Set("Location", fuelStation.Location)
                .Set("Opentime", fuelStation.Opentime).Set("Closetime", fuelStation.Closetime).Set("UserId", fuelStation.UserId);
            _Collection.UpdateOne(filter, update);
            return new JsonResult("Update Successfull");
        }

        public JsonResult Delete(int id)
        {
            var filter = Builders<FuelStations>.Filter.Eq("FuelStationId", id);
            _Collection.DeleteOne(filter);
            return new JsonResult("Delete Successfull");
        }

        public JsonResult GetByLocation(string Location)
        {
            var filter = Builders<FuelStations>.Filter.Eq("Location", Location);
            var datalist = _Collection.Find(filter).ToList();
            return new JsonResult(datalist);
        }

        public JsonResult GetByUserID(int UserId)
        {
            var filter = Builders<FuelStations>.Filter.Eq("UserId", UserId);
            var datalist = _Collection.Find(filter).ToList();
            return new JsonResult(datalist);
        }

        public JsonResult GetByFuelStationID(int FuelStationID)
        {
            var filter = Builders<FuelStations>.Filter.Eq("FuelStationId", FuelStationID);
            var datalist = _Collection.Find(filter).ToList().First();
            return new JsonResult(datalist);
        }


    }
}
