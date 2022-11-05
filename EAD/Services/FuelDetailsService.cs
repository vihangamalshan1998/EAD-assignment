using EAD.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Options;
using Microsoft.VisualBasic.FileIO;
using MongoDB.Driver;
using static MongoDB.Driver.WriteConcern;

namespace EAD.Services
{
    public class FuelDetailsService
    {
        private readonly IMongoCollection<FuelDetails> _Collection;
        private readonly IMongoCollection<QueueDetails> _QueueCollection;
        public FuelDetailsService(IOptions<QueueDbConnection> queueDatabaseSettings, IOptions<FuelDbConnection> fuelDatabaseSettings)
        {
            var mongoClient = new MongoClient(
                queueDatabaseSettings.Value.ConnectionString);

            var mongoDatabase = mongoClient.GetDatabase(
                queueDatabaseSettings.Value.DatabaseName);

            _Collection = mongoDatabase.GetCollection<FuelDetails>(
                fuelDatabaseSettings.Value.FuelCollectionName);

            _QueueCollection = mongoDatabase.GetCollection<QueueDetails>(
                queueDatabaseSettings.Value.QueueCollectionName); ;
        }

        public JsonResult Get()
        {
            var datalist = _Collection.AsQueryable();
            return new JsonResult(datalist);
        }

        public JsonResult Post(FuelDetails fuelDetails)
        {
            int lastFuelDetailsId = _Collection.AsQueryable().Count();
            fuelDetails.FuelDetailId = lastFuelDetailsId + 1;
            _Collection.InsertOne(fuelDetails);
            return new JsonResult("Adding Successfull");
        }

        public JsonResult Put(FuelDetails fuelDetails)
        {
            var filter = Builders<FuelDetails>.Filter.Eq("FuelDetailId", fuelDetails.FuelDetailId);
            var update = Builders<FuelDetails>.Update.Set("FuelType", fuelDetails.FuelType).Set("Capacity", fuelDetails.Capacity).Set("IsArrival", fuelDetails.IsArrival);
            _Collection.UpdateOne(filter, update);
            return new JsonResult("Update Successfull");
        }

        public JsonResult Delete(int id)
        {
            var filter = Builders<FuelDetails>.Filter.Eq("FuelDetailId", id);
            _Collection.DeleteOne(filter);
            return new JsonResult("Delete Successfull");
        }

        public JsonResult AvailableFuelQuotation(string fuelType, int fuelStationId)
        {
            var filter = Builders<FuelDetails>.Filter.Eq("FuelType", fuelType) & Builders<FuelDetails>.Filter.Eq("IsArrival", true)
                & Builders<FuelDetails>.Filter.Eq("FuelStationId", fuelStationId);
            var datalist = _Collection.Find(filter).ToList();

            double total = datalist.Sum(item => item.Capacity);

            var Finished_filter = Builders<QueueDetails>.Filter.Eq("FuelType", fuelType)
                 & Builders<QueueDetails>.Filter.Eq("FuelStationId", fuelStationId) & Builders<QueueDetails>.Filter.Eq("Status", "Finished");
            double Finished_count = _QueueCollection.Find(Finished_filter).Count();

            var IntheQueue_filter = Builders<QueueDetails>.Filter.Eq("FuelType", fuelType)
                & Builders<QueueDetails>.Filter.Eq("FuelStationId", fuelStationId) & Builders<QueueDetails>.Filter.Eq("Status", "IntheQueue");
            double IntheQueue_count = _QueueCollection.Find(IntheQueue_filter).Count();

            var forOneVehicle = 20;

            double finished_fuel = Finished_count * forOneVehicle;
            double remaning_fuel = total - finished_fuel;

            var remaningVehicleCount = remaning_fuel / forOneVehicle;
            remaningVehicleCount = remaningVehicleCount - IntheQueue_count;

            var values = new Dictionary<string, double>();
            values.Add("Capacity", remaning_fuel);
            values.Add("Total_Available", remaningVehicleCount);
            values.Add("In_Queue_count", IntheQueue_count);
            values.Add(fuelType, 1);

            return new JsonResult(values);
        }
        public JsonResult FuelDetailsHistory(int fuelStationId)
        {
            var filter = Builders<FuelDetails>.Filter.Eq("FuelStationId", fuelStationId);
            var datalist = _Collection.Find(filter).ToList();
            return new JsonResult(datalist);
        }
    }
}
