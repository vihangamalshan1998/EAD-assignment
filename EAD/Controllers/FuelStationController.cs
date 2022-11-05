using EAD.Models;
using EAD.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Bson;
using MongoDB.Driver;

namespace EAD.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class FuelStationController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        private readonly FuelStationsService _stationService;

        public FuelStationController(FuelStationsService fuelStationService) =>
            _stationService = fuelStationService;


        [HttpGet]
        public JsonResult Get()
        {
            var jsonRes = _stationService.Get();
            return jsonRes;
        }
        [HttpPost]
        public JsonResult Post(FuelStations fuelStation)
        {
            var jsonRes = _stationService.Post(fuelStation);
            return jsonRes;
        }

        [HttpPut]
        public JsonResult Put(FuelStations fuelStation)
        {
            var res = _stationService.Put(fuelStation);
            return res;
        }

        [HttpDelete("{id}")]
        public JsonResult Delete(int id)
        {
            var res = _stationService.Delete(id);
            return res;
        }

        [HttpGet("{Location}")]
        public JsonResult GetByLocation(string Location)
        {
            var res = _stationService.GetByLocation(Location);
            return res;
        }

        [HttpGet("{UserId}/getRelatedFuelStations")]
        public JsonResult GetByUserID(int UserId)
        {
            var res = _stationService.GetByUserID(UserId);
            return res;
        }


        [HttpGet("{FuelStationID}/getRelatedDataUsingID")]
        public JsonResult GetByFuelStationID(int FuelStationID)
        {
            var res = _stationService.GetByFuelStationID(FuelStationID);
            return res;
        }
    }
}
