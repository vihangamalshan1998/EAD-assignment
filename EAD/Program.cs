using EAD.Models;
using EAD.Services;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container..
builder.Services.Configure<StationDbConnection>(
    builder.Configuration.GetSection("StationDatabase"));

builder.Services.Configure<QueueDbConnection>(
    builder.Configuration.GetSection("QueueDatabase"));

builder.Services.Configure<UserDbConnection>(
    builder.Configuration.GetSection("UserDatabase"));

builder.Services.Configure<FuelDbConnection>(
    builder.Configuration.GetSection("FuelDatabase"));

builder.Services.AddSingleton<FuelDetailsService>();
builder.Services.AddSingleton<QueueDetailsService>();
builder.Services.AddSingleton<FuelStationsService>();
builder.Services.AddSingleton<UsersService>();

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseAuthorization();

app.MapControllers();

app.Run();
