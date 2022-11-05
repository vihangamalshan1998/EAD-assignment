using System.Collections.Generic;
using EAD.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Options;
using Microsoft.VisualBasic.FileIO;
using MongoDB.Driver;

namespace EAD.Services
{
    public class QueueDetailsService
    {
        private readonly IMongoCollection<FuelDetails> _Collection;
        private readonly IMongoCollection<QueueDetails> _QueueCollection;
        public QueueDetailsService(IOptions<QueueDbConnection> queueDatabaseSettings, IOptions<FuelDbConnection> fuelDatabaseSettings)
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
            var datalist = _QueueCollection.AsQueryable();
            return new JsonResult(datalist);
        }

        public JsonResult Post(QueueDetails queueDetails)
        {
            int lastQueueDetailsId = _QueueCollection.AsQueryable().Count();
            queueDetails.QueueId = lastQueueDetailsId + 1;
            _QueueCollection.InsertOne(queueDetails);

            var filter1 = Builders<QueueDetails>.Filter.Eq("UserId", queueDetails.UserId);
            var datalist = _QueueCollection.Find(filter1).ToList().LastOrDefault();
            double status_value = 0;
            if (datalist != null)
            {
                var status = datalist.Status;
                if (status == "IntheQueue")
                {
                    status_value = 0;
                }
                else if (status == "RemoveFromMiddle")
                {
                    status_value = 1;
                }
                else if (status == "Finished")
                {
                    status_value = 2;
                }
            }
            else
            {
                status_value = 3;
            }

            var filter2 = Builders<FuelDetails>.Filter.Eq("FuelType", queueDetails.FuelType) & Builders<FuelDetails>.Filter.Eq("IsArrival", true)
                & Builders<FuelDetails>.Filter.Eq("FuelStationId", queueDetails.FuelStationId);
            var datalist2 = _Collection.Find(filter2).ToList();

            double total = datalist2.Sum(item => item.Capacity);

            var Finished_filter = Builders<QueueDetails>.Filter.Eq("FuelType", queueDetails.FuelType)
                 & Builders<QueueDetails>.Filter.Eq("FuelStationId", queueDetails.FuelStationId) & Builders<QueueDetails>.Filter.Eq("Status", "Finished");
            double Finished_count = _QueueCollection.Find(Finished_filter).Count();

            var IntheQueue_filter = Builders<QueueDetails>.Filter.Eq("FuelType", queueDetails.FuelType)
                & Builders<QueueDetails>.Filter.Eq("FuelStationId", queueDetails.FuelStationId) & Builders<QueueDetails>.Filter.Eq("Status", "IntheQueue");
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
            values.Add(queueDetails.FuelType, 1);
            values.Add("current_status", status_value);

            return new JsonResult(values);
        }

        public JsonResult Put(QueueDetails queueDetails)
        {
            var filter = Builders<QueueDetails>.Filter.Eq("QueueId", queueDetails.QueueId);
            var update = Builders<QueueDetails>.Update.Set("VehicleType", queueDetails.VehicleType).Set("FuelType", queueDetails.FuelType)
                .Set("Status", queueDetails.Status).Set("FuelStationId", queueDetails.FuelStationId).Set("UserId", queueDetails.UserId);
            _QueueCollection.UpdateOne(filter, update);
            return new JsonResult("Update Successfull");
        }

        public JsonResult Delete(int id)
        {
            var filter = Builders<QueueDetails>.Filter.Eq("QueueId", id);
            _QueueCollection.DeleteOne(filter);
            return new JsonResult("Delete Successfull");
        }

        public double Queue(string vehicleType, string fuelType, int fuelStationId)
        {
            var filter = Builders<QueueDetails>.Filter.Eq("VehicleType", vehicleType) & Builders<QueueDetails>.Filter.Eq("FuelType", fuelType)
                 & Builders<QueueDetails>.Filter.Eq("FuelStationId", fuelStationId) & Builders<QueueDetails>.Filter.Eq("Status", "IntheQueue");
            double count = _QueueCollection.Find(filter).Count();
            return count;
        }

        public JsonResult GetByUserID(int UserId, string fuelType, int fuelStationId)
        {
            var filter1 = Builders<QueueDetails>.Filter.Eq("UserId", UserId);
            var datalist = _QueueCollection.Find(filter1).ToList().LastOrDefault();
            double status_value = 0;
            if (datalist != null)
            {
                var status = datalist.Status;
                if (status == "IntheQueue")
                {
                    status_value = 0;
                }
                else if (status == "RemoveFromMiddle")
                {
                    status_value = 1;
                }
                else if (status == "Finished")
                {
                    status_value = 2;
                }
            }
            else
            {
                status_value = 3;
            }

            var filter2 = Builders<FuelDetails>.Filter.Eq("FuelType", fuelType) & Builders<FuelDetails>.Filter.Eq("IsArrival", true)
                & Builders<FuelDetails>.Filter.Eq("FuelStationId", fuelStationId);
            var datalist2 = _Collection.Find(filter2).ToList();

            double total = datalist2.Sum(item => item.Capacity);

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
            values.Add("current_status", status_value);


            return new JsonResult(values);
        }

        public JsonResult UpdateQueue(int UserId, string new_status,string fuelType,int fuelStationId)
        {
            var filter = Builders<QueueDetails>.Filter.Eq("UserId", UserId) & Builders<QueueDetails>.Filter.Eq("Status", "IntheQueue")
                & Builders<QueueDetails>.Filter.Eq("FuelStationId", fuelStationId);
            var update = Builders<QueueDetails>.Update.Set("Status", new_status);
            _QueueCollection.UpdateOne(filter, update);


            var filter1 = Builders<QueueDetails>.Filter.Eq("UserId", UserId);
            var datalist = _QueueCollection.Find(filter1).ToList().LastOrDefault();
            double status_value = 0;
            if (datalist != null)
            {
                var status = datalist.Status;
                if (status == "IntheQueue")
                {
                    status_value = 0;
                }
                else if (status == "RemoveFromMiddle")
                {
                    status_value = 1;
                }
                else if (status == "Finished")
                {
                    status_value = 2;
                }
            }
            else
            {
                status_value = 3;
            }

            var filter2 = Builders<FuelDetails>.Filter.Eq("FuelType", fuelType) & Builders<FuelDetails>.Filter.Eq("IsArrival", true)
                & Builders<FuelDetails>.Filter.Eq("FuelStationId", fuelStationId);
            var datalist2 = _Collection.Find(filter2).ToList();

            double total = datalist2.Sum(item => item.Capacity);

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
            values.Add("current_status", status_value);

            return new JsonResult(values);
        }

    }
}
