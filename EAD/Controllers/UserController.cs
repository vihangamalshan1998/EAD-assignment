using EAD.Models;
using EAD.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;

namespace EAD.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        private readonly UsersService _userService;

        public UserController(UsersService userService)
        {
            _userService = userService;
        }


        [HttpGet]
        public JsonResult Get()
        {
            var jsonRes = _userService.Get();
            return jsonRes;
        }
        [HttpPost]
        public JsonResult Post(Users users)
        {
            var jsonRes = _userService.Post(users);
            return jsonRes;
        }

        [HttpPut]
        public JsonResult Put(Users users)
        {
            var jsonRes = _userService.Put(users);
            return jsonRes;
        }

        [HttpDelete("{id}")]
        public JsonResult Delete(int id)
        {
            var jsonRes = _userService.Delete(id);
            return jsonRes;
        }


        [HttpGet("{UserName}/{Password}")]
        public JsonResult Queue(string UserName, string Password)
        {
            var jsonRes = _userService.Queue(UserName, Password);
            return jsonRes;
        }
    }
}
