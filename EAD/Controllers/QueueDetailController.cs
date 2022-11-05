using EAD.Models;
using EAD.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.VisualBasic.FileIO;
using MongoDB.Driver;

namespace EAD.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class QueueDetailController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        private readonly QueueDetailsService _queueService;

        public QueueDetailController(QueueDetailsService queueService)
        {
            _queueService = queueService;

        }

        [HttpGet]
        public JsonResult Get()
        {
            var datalist = _queueService.Get();
            return datalist;
        }

        [HttpPost]
        public JsonResult Post(QueueDetails queueDetails)
        {
            var datalist = _queueService.Post(queueDetails);
            return datalist;
        }

        [HttpPut]
        public JsonResult Put(QueueDetails queueDetails)
        {
            var datalist = _queueService.Put(queueDetails);
            return datalist;
        }

        [HttpDelete("{id}")]
        public JsonResult Delete(int id)
        {
            var datalist = _queueService.Delete(id);
            return datalist;
        }

        [HttpGet("{vehicleType}/{fuelType}/{fuelStationId}")]
        public double Queue(string vehicleType, string fuelType, int fuelStationId)
        {
            var datalist = _queueService.Queue(vehicleType, fuelType, fuelStationId);
            return datalist;
        }

        [HttpGet("{UserId}/{fuelType}/{fuelStationId}/getDetails")]
        public JsonResult GetByUserID(int UserId, string fuelType, int fuelStationId)
        {
            var datalist = _queueService.GetByUserID(UserId, fuelType, fuelStationId);
            return datalist;
        }


        [HttpPut("{UserId}/{status}/{fuelType}/{fuelStationId}")]
        public JsonResult UpdateQueue(int UserId, string status, string fuelType, int fuelStationId)
        {
            var datalist = _queueService.UpdateQueue(UserId,status, fuelType,fuelStationId);
            return datalist;
        }
    }
}
