using System.Text;
using EAD.Models;
using EAD.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;

namespace EAD.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class FuelDetailController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        private readonly FuelDetailsService _fuelService;

        public FuelDetailController(FuelDetailsService fuelService)
        {
            _fuelService = fuelService;
        }

        [HttpGet]
        public JsonResult Get()
        {
            var datalist = _fuelService.Get();
            return datalist;
        }

        [HttpPost]
        public JsonResult Post(FuelDetails fuelDetails)
        {
            var jsonRes = _fuelService.Post(fuelDetails);
            return jsonRes;
        }

        [HttpPut]
        public JsonResult Put(FuelDetails fuelDetails)
        {
            var jsonRes = _fuelService.Post(fuelDetails);
            return jsonRes;
        }

        [HttpDelete("{id}")]
        public JsonResult Delete(int id)
        {
            var jsonRes = _fuelService.Delete(id);
            return jsonRes;
        }


        [HttpGet("{fuelType}/{fuelStationId}")]
        public JsonResult AvailableFuelQuotation(string fuelType,int fuelStationId)
        {
            var jsonRes = _fuelService.AvailableFuelQuotation(fuelType, fuelStationId);
            return jsonRes;
        }

        [HttpGet("{fuelStationId}/getAlltheData")]
        public JsonResult FuelDetailsHistory(int fuelStationId)
        {
            var jsonRes = _fuelService.FuelDetailsHistory(fuelStationId);
            return jsonRes;
        }
    }
}
