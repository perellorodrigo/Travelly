package com.example.rodrigo.travelly;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.example.rodrigo.travelly.models.Expense;
import com.example.rodrigo.travelly.models.Reminder;
import com.example.rodrigo.travelly.models.Trip;
import com.example.rodrigo.travelly.models.User;
import com.example.rodrigo.travelly.models.Vehicle;
import com.example.rodrigo.travelly.models.Waypoint;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 6;

    //DATABASE NAME
    private static final String DATABASE_NAME = "TravellyDatabase.db";
    //-------------

    // USER TABLE DEFINITIONS:

    // USER TABLE NAME:
    private static final String TABLE_USER = "users";

    // USER TABLE COLUMN NAMES:
    private static final String USER_COLUMN_USER_ID = "user_id";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_NAME = "name";
    private static final String USER_COLUMN_HOMETOWN = "hometown";

    // CREATE USER TABLE:
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + USER_COLUMN_USER_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_COLUMN_EMAIL     + " TEXT,"
            + USER_COLUMN_PASSWORD  + " TEXT,"
            + USER_COLUMN_NAME      + " TEXT,"
            + USER_COLUMN_HOMETOWN  + " TEXT" + ")";

    // DROP USER TABLE:
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    //--------------------------------------------------------------------

    // VEHICLE TABLE DEFINITIONS
    private static final String TABLE_VEHICLE = "vehicles";

    // VEHICLE TABLE COLUMN NAMES
    private static final String VEHICLE_COLUMN_VEHICLE_ID = "vehicle_id";
    private static final String VEHICLE_COLUMN_NAME = "name";
    private static final String VEHICLE_COLUMN_FUEL_CONSUMPTION = "fuel_consumption";
    private static final String VEHICLE_COLUMN_OWNER_ID = "owner_id";

    // CREATE VEHICLE TABLE
    private String CREATE_VEHICLE_TABLE = "CREATE TABLE " + TABLE_VEHICLE + "("
            + VEHICLE_COLUMN_VEHICLE_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + VEHICLE_COLUMN_NAME     + " TEXT,"
            + VEHICLE_COLUMN_FUEL_CONSUMPTION  + " REAL,"
            + VEHICLE_COLUMN_OWNER_ID      + " INTEGER,"
            + " FOREIGN KEY (" + VEHICLE_COLUMN_OWNER_ID + ") REFERENCES " + TABLE_USER + " ("+ USER_COLUMN_USER_ID  + "))";

    // DROP VEHICLE TABLE
    private String DROP_VEHICLE_TABLE = "DROP TABLE IF EXISTS " + TABLE_VEHICLE;

    //--------------------------------------------------------------------

    // TRIP TABLE DEFINITIONS

    // TRIP TABLE NAME:
    private static final String TABLE_TRIP = "trips";

    // TRIP TABLE COLUMN NAMES:
    private static final String TRIP_COLUMN_TRIP_ID = "trip_id";
    private static final String TRIP_COLUMN_NAME = "name";
    private static final String TRIP_COLUMN_START_DATE = "start_date";
    private static final String TRIP_COLUMN_ORIGIN_LAT = "origin_lat";
    private static final String TRIP_COLUMN_ORIGIN_LNG = "origin_lng";
    private static final String TRIP_COLUMN_DESTINATION_LAT = "destination_lat";
    private static final String TRIP_COLUMN_DESTINATION_LNG = "destination_lng";
    private static final String TRIP_COLUMN_VEHICLE_ID = "vehicle_id";
    private static final String TRIP_COLUMN_USER_ID = "user_id";

    // CREATE TRIP TABLE
    private String CREATE_TRIP_TABLE = "CREATE TABLE " + TABLE_TRIP + "("
            + TRIP_COLUMN_TRIP_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TRIP_COLUMN_NAME             + " TEXT,"
            + TRIP_COLUMN_START_DATE       + " TEXT,"
            + TRIP_COLUMN_ORIGIN_LAT       + " REAL,"
            + TRIP_COLUMN_ORIGIN_LNG       + " REAL,"
            + TRIP_COLUMN_DESTINATION_LAT  + " REAL,"
            + TRIP_COLUMN_DESTINATION_LNG  + " REAL,"
            + TRIP_COLUMN_VEHICLE_ID       + " INTEGER,"
            + TRIP_COLUMN_USER_ID          + " INTEGER,"
            + " FOREIGN KEY (" + TRIP_COLUMN_VEHICLE_ID + ") REFERENCES " + TABLE_VEHICLE + " ("+ VEHICLE_COLUMN_VEHICLE_ID  + "),"
            + " FOREIGN KEY (" + TRIP_COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + " ("+ USER_COLUMN_USER_ID  + "))";

    // DROP TRIP TABLE
    private String DROP_TRIP_TABLE = "DROP TABLE IF EXISTS " + TABLE_TRIP;
    //---------------------------------------------------------------------------

    // WAYPOINTS TABLE DEFINITIONS
    private static final String TABLE_WAYPOINT = "waypoints";

    // WAYPOINTS TABLE COLUMN NAMES
    private static final String WAYPOINT_COLUMN_WAYPOINT_ID = "waypoint_id";
    private static final String WAYPOINT_COLUMN_DESCRIPTION = "description";
    private static final String WAYPOINT_COLUMN_EXPECTED_DATE = "expected_date";
    private static final String WAYPOINT_COLUMN_LAT = "latitude";
    private static final String WAYPOINT_COLUMN_LNG = "longitude";
    private static final String WAYPOINT_COLUMN_TRIP_ID = "trip_id";

    // CREATE WAYPOINTS TABLE
    private String CREATE_WAYPOINT_TABLE = "CREATE TABLE " + TABLE_WAYPOINT + "("
            + WAYPOINT_COLUMN_WAYPOINT_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + WAYPOINT_COLUMN_DESCRIPTION     + " TEXT,"
            + WAYPOINT_COLUMN_EXPECTED_DATE     + " TEXT,"
            + WAYPOINT_COLUMN_LAT     + " REAL,"
            + WAYPOINT_COLUMN_LNG  + " REAL,"
            + WAYPOINT_COLUMN_TRIP_ID      + " INTEGER,"
            + " FOREIGN KEY (" + WAYPOINT_COLUMN_TRIP_ID + ") REFERENCES " + TABLE_TRIP + " ("+ TRIP_COLUMN_TRIP_ID  + "))";

    // DROP WAYPOINTS TABLE
    private String DROP_WAYPOINT_TABLE = "DROP TABLE IF EXISTS " + TABLE_WAYPOINT;

    //--------------------------------------------------------------------
    // WAYPOINTS TABLE DEFINITIONS
    private static final String TABLE_EXPENSE = "expenses";

    // WAYPOINTS TABLE COLUMN NAMES
    private static final String EXPENSE_COLUMN_EXPENSE_ID = "expense_id";
    private static final String EXPENSE_COLUMN_DESCRIPTION = "description";
    private static final String EXPENSE_COLUMN_AMOUNT = "amount";
    private static final String EXPENSE_COLUMN_DATE = "date";
    private static final String EXPENSE_COLUMN_TRIP_ID = "trip_id";


    // CREATE WAYPOINTS TABLE
    private String CREATE_EXPENSE_TABLE = "CREATE TABLE " + TABLE_EXPENSE + "("
            + EXPENSE_COLUMN_EXPENSE_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + EXPENSE_COLUMN_DESCRIPTION   + " TEXT,"
            + EXPENSE_COLUMN_AMOUNT                + " REAL,"
            + EXPENSE_COLUMN_DATE          + " TEXT,"
            + EXPENSE_COLUMN_TRIP_ID               + " INTEGER,"
            + " FOREIGN KEY (" + EXPENSE_COLUMN_TRIP_ID + ") REFERENCES " + TABLE_TRIP + " ("+ TRIP_COLUMN_TRIP_ID  + "))";

    // DROP WAYPOINTS TABLE
    private String DROP_EXPENSE_TABLE = "DROP TABLE IF EXISTS " + TABLE_EXPENSE;


    //---------------------------------------------------------------------
    // REMINDER TABLE DEFINITIONS
    private static final String TABLE_REMINDER = "reminders";

    // REMINDER TABLE COLUMN NAMES
    private static final String REMINDER_COLUMN_REMINDER_ID = "reminder_id";
    private static final String REMINDER_COLUMN_TITLE = "title";
    private static final String REMINDER_COLUMN_DESCRIPTION = "description";
    private static final String REMINDER_COLUMN_TRIP_ID = "trip_id";


    private String CREATE_REMINDER_TABLE = "CREATE TABLE " + TABLE_REMINDER + "("
            + REMINDER_COLUMN_REMINDER_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + REMINDER_COLUMN_TITLE                 + " TEXT,"
            + REMINDER_COLUMN_DESCRIPTION           + " TEXT,"
            + REMINDER_COLUMN_TRIP_ID               + " INTEGER,"
            + " FOREIGN KEY (" + REMINDER_COLUMN_TRIP_ID + ") REFERENCES " + TABLE_TRIP + " ("+ TRIP_COLUMN_TRIP_ID  + "))";

    // DROP REMINDERS TABLE
    private String DROP_REMINDER_TABLE = "DROP TABLE IF EXISTS " + TABLE_REMINDER;



    //---------------------------------------------------------------------
    private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_VEHICLE_TABLE);
        db.execSQL(CREATE_TRIP_TABLE);
        db.execSQL(CREATE_WAYPOINT_TABLE);
        db.execSQL(CREATE_EXPENSE_TABLE);
        db.execSQL(CREATE_REMINDER_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_REMINDER_TABLE);
        db.execSQL(DROP_EXPENSE_TABLE);
        db.execSQL(DROP_WAYPOINT_TABLE);
        db.execSQL(DROP_TRIP_TABLE);
        db.execSQL(DROP_VEHICLE_TABLE);
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }
    public User addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_EMAIL,user.getEmail());
        values.put(USER_COLUMN_PASSWORD, user.getPassword());
        values.put(USER_COLUMN_NAME, user.getName());
        values.put(USER_COLUMN_HOMETOWN, user.getHometown());

        int rowID = (int) db.insert(TABLE_USER,null,values);
        db.close();

        if (rowID != -1)
            user.setId(rowID);
        else{
            user = null;
        }

        return user;

    }
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                USER_COLUMN_USER_ID,
                USER_COLUMN_EMAIL,
                USER_COLUMN_PASSWORD,
                USER_COLUMN_NAME,
                USER_COLUMN_HOMETOWN
        };


        String selection = USER_COLUMN_EMAIL + " = ?";

        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        User user = null;


        if(cursor.moveToFirst()){
            user = new User();
            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(USER_COLUMN_USER_ID))));
            user.setEmail(cursor.getString(cursor.getColumnIndex(USER_COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(USER_COLUMN_PASSWORD)));
            user.setName(cursor.getString(cursor.getColumnIndex(USER_COLUMN_NAME)));
            user.setHometown(cursor.getString(cursor.getColumnIndex(USER_COLUMN_HOMETOWN)));
        }
        cursor.close();
        db.close();

        return user;
    }
    public List<User> getAllUsers(){
        String[] columns = {
                USER_COLUMN_USER_ID,
                USER_COLUMN_EMAIL,
                USER_COLUMN_PASSWORD,
                USER_COLUMN_NAME,
                USER_COLUMN_HOMETOWN
        };

        String sortOrder = USER_COLUMN_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER,columns,null,null,null,null,sortOrder);

        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(USER_COLUMN_USER_ID))));
                user.setEmail(cursor.getString(cursor.getColumnIndex(USER_COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(USER_COLUMN_PASSWORD)));
                user.setName(cursor.getString(cursor.getColumnIndex(USER_COLUMN_NAME)));
                user.setHometown(cursor.getString(cursor.getColumnIndex(USER_COLUMN_HOMETOWN)));
                userList.add(user);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }
    public boolean checkUser(String email){
        String[] columns = {USER_COLUMN_USER_ID};

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = USER_COLUMN_EMAIL + " = ?";

        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount > 0)
            return true;

        return false;
    }
    public boolean checkUser(String email, String password){
        String[] columns = {USER_COLUMN_USER_ID};
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = USER_COLUMN_EMAIL + " = ?" + " AND " + USER_COLUMN_PASSWORD + " = ?";

        String[] selectionArgs = {email,password};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount > 0)
            return true;

        return false;
    }
    public Trip addTrip(Trip trip, int userID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TRIP_COLUMN_NAME,trip.getName());

        values.put(TRIP_COLUMN_START_DATE, dateFormat.format(trip.getStartDate()));
        values.put(TRIP_COLUMN_ORIGIN_LAT,trip.getOriginPosition().latitude);
        values.put(TRIP_COLUMN_ORIGIN_LNG,trip.getOriginPosition().longitude);
        values.put(TRIP_COLUMN_DESTINATION_LAT,trip.getDestinationPosition().latitude);
        values.put(TRIP_COLUMN_DESTINATION_LNG,trip.getDestinationPosition().longitude);
        values.put(TRIP_COLUMN_VEHICLE_ID, trip.getVehicle().getId());
        values.put(TRIP_COLUMN_USER_ID, userID);

        int rowID = (int) db.insert(TABLE_TRIP,null,values);
        db.close();

        if (rowID != -1) {
            trip.setId(rowID);
            AppData.loggedUserTrips.add(trip); //Update data in static class
        }
        else{
            trip = null;
        }


        return trip;
    }
    public ArrayList<Trip> getTripsByUser(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        int tripID;

        String[] columns = {
                TRIP_COLUMN_TRIP_ID,
                TRIP_COLUMN_NAME,
                TRIP_COLUMN_START_DATE,
                TRIP_COLUMN_ORIGIN_LAT,
                TRIP_COLUMN_ORIGIN_LNG,
                TRIP_COLUMN_DESTINATION_LAT,
                TRIP_COLUMN_DESTINATION_LNG,
                TRIP_COLUMN_VEHICLE_ID,
                TRIP_COLUMN_USER_ID
        };

        String sortOrder = TRIP_COLUMN_NAME + " ASC";
        ArrayList<Trip> tripList = new ArrayList<Trip>();
        String selection = TRIP_COLUMN_USER_ID + " = ?";

        String[] selectionArgs = {Integer.toString(userID)};

        Cursor cursor = db.query(TABLE_TRIP,columns,selection,selectionArgs,null,null,sortOrder);

        if(cursor.moveToFirst()){
            do{
                Trip trip = new Trip();
                tripID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TRIP_COLUMN_TRIP_ID)));
                trip.setId(tripID); //Will be used to get the waypoints, reminders and expenses
                trip.setName(cursor.getString(cursor.getColumnIndex(TRIP_COLUMN_NAME)));

                try {
                    trip.setStartDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(TRIP_COLUMN_START_DATE))));
                }catch (ParseException ex)
                {
                    Log.d("PARSE_EX","Error parsing: " + ex);
                    trip.setStartDate(new Date(2000,1,1));
                }

                LatLng origin = new LatLng(
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(TRIP_COLUMN_ORIGIN_LAT))),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(TRIP_COLUMN_ORIGIN_LNG)))
                );

                LatLng destination = new LatLng(
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(TRIP_COLUMN_DESTINATION_LAT))),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(TRIP_COLUMN_DESTINATION_LNG)))
                );

                trip.setOriginPosition(origin);
                trip.setDestinationPosition(destination);

                //TODO Add vehicle
                int vehicleID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TRIP_COLUMN_VEHICLE_ID)));
                trip.setVehicle(getVehicleByID(vehicleID));

                trip.setWaypoints(getWaypointsByTripID(tripID));
                tripList.add(trip);
            }while(cursor.moveToNext());


        }
        cursor.close();


        db.close();

        return tripList;
    }
    public Vehicle addVehicle(Vehicle vehicle, int ownerID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(VEHICLE_COLUMN_NAME,vehicle.getVehicleName());
        values.put(VEHICLE_COLUMN_FUEL_CONSUMPTION, vehicle.getFuelConsumption());
        values.put(VEHICLE_COLUMN_OWNER_ID,ownerID);

        int rowID = (int) db.insert(TABLE_VEHICLE,null,values);
        if (rowID != -1) {
            vehicle.setId(rowID);
            AppData.loggedUserVehicles.add(vehicle);
        }
        else{
            vehicle = null;
        }

        db.close();

        // If added to database, return the vehicle with ID updated, so it can be consistent in the application
        // If couldn't add, return null
        return vehicle;
    }
    public Vehicle getVehicleByID(int vehicleID){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                VEHICLE_COLUMN_VEHICLE_ID,
                VEHICLE_COLUMN_NAME,
                VEHICLE_COLUMN_FUEL_CONSUMPTION,
                VEHICLE_COLUMN_OWNER_ID
        };


        String selection = VEHICLE_COLUMN_VEHICLE_ID + " = ?";

        String[] selectionArgs = {Integer.toString(vehicleID)};

        String sortOrder = VEHICLE_COLUMN_VEHICLE_ID + " ASC";

        Cursor cursor = db.query(TABLE_VEHICLE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        Vehicle vehicle = null;

        if(cursor.moveToFirst()){
            vehicle = new Vehicle(
                    cursor.getString(cursor.getColumnIndex(VEHICLE_COLUMN_NAME)),
                    Float.parseFloat(cursor.getString(cursor.getColumnIndex(VEHICLE_COLUMN_FUEL_CONSUMPTION))));

            vehicle.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(VEHICLE_COLUMN_VEHICLE_ID))));
        }
        cursor.close();
        db.close();

        return vehicle;
    }
    @TargetApi(Build.VERSION_CODES.N)
    public boolean deleteVehicleByID(final int vehicleID){
        SQLiteDatabase db = this.getWritableDatabase();

        boolean inserted = db.delete(TABLE_VEHICLE,VEHICLE_COLUMN_VEHICLE_ID + "=" + vehicleID,null) > 0;
        db.close();

        if(inserted)
            AppData.loggedUserVehicles.removeIf(s -> s.getId() == vehicleID);

        return inserted;

    }
    public ArrayList<Vehicle> getVehiclesByUserID(int userID){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                VEHICLE_COLUMN_VEHICLE_ID,
                VEHICLE_COLUMN_NAME,
                VEHICLE_COLUMN_FUEL_CONSUMPTION,
                VEHICLE_COLUMN_OWNER_ID
        };


        String selection = VEHICLE_COLUMN_OWNER_ID + " = ?";

        String[] selectionArgs = {Integer.toString(userID)};


        Cursor cursor = db.query(TABLE_VEHICLE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
                );

        Vehicle vehicle = null;
        ArrayList<Vehicle> vehicles = null;

        if(cursor.moveToFirst()){
            vehicles = new ArrayList<>();
            do {

                vehicle = new Vehicle(
                        cursor.getString(cursor.getColumnIndex(VEHICLE_COLUMN_NAME)),
                        Float.parseFloat(cursor.getString(cursor.getColumnIndex(VEHICLE_COLUMN_FUEL_CONSUMPTION))));

                vehicle.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(VEHICLE_COLUMN_VEHICLE_ID))));
                vehicles.add(vehicle);
                Log.d("DATABASE_HELPER_LOG","FOUND");
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return vehicles;

    }
    public Waypoint addWaypoint(Waypoint waypoint, int tripID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(WAYPOINT_COLUMN_DESCRIPTION, waypoint.getDescription());
        values.put(WAYPOINT_COLUMN_EXPECTED_DATE, dateFormat.format(waypoint.getExpectedDate()));
        values.put(WAYPOINT_COLUMN_LAT, waypoint.getPosition().latitude);
        values.put(WAYPOINT_COLUMN_LNG, waypoint.getPosition().longitude);
        values.put(WAYPOINT_COLUMN_TRIP_ID, tripID);

        int rowID = (int) db.insert(TABLE_WAYPOINT,null,values);
        db.close();

        if (rowID != -1) {
            waypoint.setId(rowID);
            AppData.loggedUserTrips = getTripsByUser(AppData.loggedUserID);
        }
        else{
            waypoint = null;
        }

        return waypoint;
    }
    public ArrayList<Waypoint> getWaypointsByTripID(int tripID){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                WAYPOINT_COLUMN_WAYPOINT_ID,
                WAYPOINT_COLUMN_DESCRIPTION,
                WAYPOINT_COLUMN_EXPECTED_DATE,
                WAYPOINT_COLUMN_LAT,
                WAYPOINT_COLUMN_LNG,
                WAYPOINT_COLUMN_TRIP_ID
        };


        String selection = WAYPOINT_COLUMN_TRIP_ID + " = ?";

        String[] selectionArgs = {Integer.toString(tripID)};

        Cursor cursor = db.query(TABLE_WAYPOINT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Waypoint waypoint = null;
        ArrayList<Waypoint> waypoints = null;

        if(cursor.moveToFirst()){
            waypoints = new ArrayList<>();
            do {
                Date expectedDate;
                try {
                    expectedDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(WAYPOINT_COLUMN_EXPECTED_DATE)));
                } catch (ParseException ex)
                {
                    Log.d("PARSE_EX","Error parsing: " + ex);
                    expectedDate = new Date(2000,1,1);
                }

                LatLng position = new LatLng(
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(WAYPOINT_COLUMN_LAT))),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(WAYPOINT_COLUMN_LNG)))
                );


                waypoint = new Waypoint(
                        cursor.getString(cursor.getColumnIndex(WAYPOINT_COLUMN_DESCRIPTION)),
                        expectedDate,
                        position
                        );

                waypoint.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(WAYPOINT_COLUMN_WAYPOINT_ID))));

                waypoints.add(waypoint);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return waypoints;
    }
    public Expense addExpense(Expense expense, int tripID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(EXPENSE_COLUMN_DESCRIPTION, expense.getDescription());
        values.put(EXPENSE_COLUMN_AMOUNT, expense.getAmount());
        values.put(EXPENSE_COLUMN_DATE, dateFormat.format(expense.getDate()));
        values.put(EXPENSE_COLUMN_TRIP_ID, tripID);

        int rowID = (int) db.insert(TABLE_EXPENSE,null,values);
        db.close();

        if (rowID != -1) {
            expense.setId(rowID);
            //Refresh the app data trips as a new expense has been added
            AppData.loggedUserTrips = getTripsByUser(AppData.loggedUserID);
        }
        else{
            expense = null;
        }

        return expense;
    }
    public ArrayList<Expense> getExpensesByTripID(int tripID){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                EXPENSE_COLUMN_EXPENSE_ID,
                EXPENSE_COLUMN_DESCRIPTION,
                EXPENSE_COLUMN_AMOUNT,
                EXPENSE_COLUMN_DATE,
                EXPENSE_COLUMN_TRIP_ID
        };


        String selection = EXPENSE_COLUMN_TRIP_ID + " = ?";

        String[] selectionArgs = {Integer.toString(tripID)};

        Cursor cursor = db.query(TABLE_EXPENSE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Expense expense = null;
        ArrayList<Expense> expenses = null;

        if(cursor.moveToFirst()){
            expenses = new ArrayList<>();
            do {
                Date expenseDate;
                try {
                    expenseDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(EXPENSE_COLUMN_DATE)));
                } catch (ParseException ex)
                {
                    Log.d("PARSE_EX","Error parsing: " + ex);
                    expenseDate = new Date(2000,1,1);
                }

                expense = new Expense(
                        cursor.getString(cursor.getColumnIndex(EXPENSE_COLUMN_DESCRIPTION)),
                        Float.parseFloat(cursor.getString(cursor.getColumnIndex(EXPENSE_COLUMN_AMOUNT))),
                        expenseDate
                );

                expense.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXPENSE_COLUMN_EXPENSE_ID))));

                expenses.add(expense);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return expenses;
    }
    public Reminder addReminder(Reminder reminder, int tripID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(REMINDER_COLUMN_TITLE, reminder.getTitle());
        values.put(REMINDER_COLUMN_DESCRIPTION, reminder.getDescription());
        values.put(REMINDER_COLUMN_TRIP_ID, tripID);

        int rowID = (int) db.insert(TABLE_REMINDER,null,values);
        db.close();

        if (rowID != -1) {
            reminder.setId(rowID);
            //Refresh the app data trips as a new expense has been added
            AppData.loggedUserTrips = getTripsByUser(AppData.loggedUserID);
        }
        else{
            reminder = null;
        }

        return reminder;
    }

    public ArrayList<Reminder> getRemindersByTripID(int tripID){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                REMINDER_COLUMN_REMINDER_ID,
                REMINDER_COLUMN_TITLE,
                REMINDER_COLUMN_DESCRIPTION,
                EXPENSE_COLUMN_TRIP_ID
        };


        String selection = REMINDER_COLUMN_TRIP_ID + " = ?";

        String[] selectionArgs = {Integer.toString(tripID)};

        Cursor cursor = db.query(TABLE_REMINDER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Reminder reminder = null;
        ArrayList<Reminder> reminders = null;

        if(cursor.moveToFirst()){
            reminders = new ArrayList<>();
            do {


                reminder = new Reminder(
                        cursor.getString(cursor.getColumnIndex(REMINDER_COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(REMINDER_COLUMN_DESCRIPTION))
                );

                reminder.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(REMINDER_COLUMN_REMINDER_ID))));

                reminders.add(reminder);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return reminders;
    }

}
