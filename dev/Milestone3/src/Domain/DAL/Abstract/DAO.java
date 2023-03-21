package Domain.DAL.Abstract;

import Domain.DAL.ConnectionHandler;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class DAO {
    private final static String SELECT_QUERY = "SELECT %s from %s where %s";
    private final static String INSERT_QUERY = "INSERT INTO %s VALUES (%s)";
    private final static String DELETE_QUERY = "DELETE FROM %s WHERE %s;";
    private final static String UPDATE_QUERY = "UPDATE %s SET %s WHERE %s";

    public static void setDBForTests(Class testClass) {
        ConnectionHandler.setUrl(String.format("jdbc:sqlite:out/test/Milestone3/SuperlyTests%s.db", testClass.getName().replace('.','_')));
        try (Connection connection = DriverManager.getConnection(ConnectionHandler.getUrl())) {
            Statement statement = connection.createStatement();
            String exec;
            String[] tables = {"InventoryTables", "SuppliersTables", "EmployeeTables", "TransportTables"};
            for (int i = 0; i < tables.length; i++) {
                File f = new File(String.format("Test/Tables/%s.txt", tables[i]));
                byte[] bf = new byte[(int) f.length()];
                new FileInputStream(f).read(bf);
                exec = new String(bf, "UTF-8");
                String[] myTables = exec.split(";");
                for (String t : myTables) {
                    statement.execute(t);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void deleteTestDB(Class testClass) {
        try {
            Files.deleteIfExists(Paths.get(String.format("out/test/Milestone3/SuperlyTests%s.db", testClass.getName().replace('.','_'))));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // properties
    protected String tableName; // this field will be valid if we will save data in tables
    private List<String> tableColumnNames;
    // constructor
    public DAO(String tableName) {
        this.tableName = tableName;
        setTableColumnsNames();
    }

    // functions

    /**
     * this function get the column names  of the table from the db
     */
    protected void setTableColumnsNames(){
        tableColumnNames = new LinkedList<>();
        try (ConnectionHandler handler = getConnectionHandler()){

            ResultSetMetaData setMetaData = executeQuery(handler.get(),String.format(SELECT_QUERY,"*",tableName,"true")).getMetaData();
            for (int i =0; i < setMetaData.getColumnCount();i++)
                tableColumnNames.add(setMetaData.getColumnLabel(i+1));
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

    }

    /**
     * executing sql Query witch does not require substitute of '?' and returns rows
     * @param connection a connection to the DB, should be close from the calling function when finish to read the result!
     * @param executeString a String to execute, should not include '?'
     * @return ResultSet that contains the {@param executeString} results
     * @throws SQLException
     */
    public ResultSet executeQuery(Connection connection,String executeString) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(executeString);
    }

    /**
     * Executing sql Query witch does not require substitute of '?' and does not return rows
     * @param executeString a String to execute, should not include '?'
     * @return true if the first result is a ResultSet object; false if it is an update count or there are no results
     * @throws SQLException
     */
    public boolean executeNonQuery(String executeString) throws SQLException {
        try (ConnectionHandler handler = getConnectionHandler()){
            Statement statement = handler.get().createStatement();
            return statement.execute(executeString);
        }
    }

    /**
     *
     * @param connection a connection to the DB, should be close from the calling function when finish to read the result!
     * @param executeStringWithReplaceable a String to execute, with '?' that should be replace with values
     * @param values values to replace in the {@param executeStringWithReplaceable}, should be in the order of the '?' in the {@param executeStringWithReplaceable}
     * @return ResultSet that contains the {@param executeStringWithReplaceable} results after the replacement of {@param values}
     * @throws SQLException
     */
    public ResultSet executeQuery(Connection connection, String executeStringWithReplaceable, List<Object> values) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(executeStringWithReplaceable);
        replaceQuestionMarks(preparedStatement,values);
        return preparedStatement.executeQuery();
    }

    /**
     *
     * @param executeStringWithReplaceable a String to execute, with '?' that should be replace with values
     * @param values values to replace in the {@param executeStringWithReplaceable}, should be in the order of the '?' in the {@param executeStringWithReplaceable}
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws SQLException
     */
    public int executeNonQuery(String executeStringWithReplaceable, List<Object> values) throws SQLException {
        try (ConnectionHandler handler = getConnectionHandler()){
            PreparedStatement preparedStatement = handler.get().prepareStatement(executeStringWithReplaceable);
            replaceQuestionMarks(preparedStatement,values);
            return preparedStatement.executeUpdate();
        }
    }

    /**
     *
     * @param connection
     * @return retuns all the rows in the table
     */
    public ResultSet select(Connection connection) throws SQLException{
        return executeQuery(connection,String.format("SELECT * from %s",tableName));
    }

    /**
     * Gets the lines with the specific id, note that the function works only with tables with id as their first column
     * @param connection a connection to the DB, should be close from the calling function when finish to read the result!
     * @param id id for the row you want to get
     * @return ResultSet witch contains the row of this id
     * @throws SQLException if the table does not have 'id' column
     */
    public ResultSet select(Connection connection, Object id) throws SQLException {
        return executeQuery(connection,String.format(SELECT_QUERY,'*',tableName,tableColumnNames.get(0) + " = ?"),Arrays.asList(id));
    }

    /**
     * gets all the rows which withstands the constraints from {@param column } = {@param value}
     * @param connection a connection to the DB, should be close from the calling function when finish to read the result!
     * @param columnsLocation the columns location in the db, starting with 1 which are going to be the conditions in the "where" statement.
     * @param values the conditions value to the column you want to get from the DB
     * @return ResultSet witch contains the row where column=value
     * @throws SQLException
     */
    public ResultSet select(Connection connection, List<Integer> columnsLocation, List<Object> values) throws SQLException {
        validateColumnsNames(columnsLocation);
        validateColumnValueRatio(columnsLocation,values);
        return executeQuery(connection,String.format(SELECT_QUERY,'*',tableName, makeColumnEqualsQuestionMarkString(columnsLocation,"AND")),values);
    }

    /**
     * gets all the rows which withstands the constraints from {@param column } in {@param value}
     * @param connection a connection to the DB, should be close from the calling function when finish to read the result!
     * @param columnLocation the column location in the db, starting with 1 which are going to be the conditions in the "where" statement.
     * @param values the conditions value to the column you want to get from the DB
     * @return ResultSet witch contains the row where column=value
     * @throws SQLException
     */
    public ResultSet select(Connection connection, Integer columnLocation, List<?> values) throws SQLException {
        validateColumnsNames(Arrays.asList(columnLocation));
        String selectColumnsString = Arrays.asList(columnLocation).stream().map(this::getColumnName).collect(Collectors.joining(", "));
        String test = values.stream().
                map(i -> String.valueOf(i)).
                collect(Collectors.joining(",", "(", ")"));
        return executeQuery(connection,String.format("SELECT * from %s where %s in %s", tableName, selectColumnsString, test));
    }

    /**
     * gets all the rows which withstands the constraints from {@param column } = {@param value}
     * @param connection a connection to the DB, should be close from the calling function when finish to read the result!
     * @param selectColumnsLocation the columns location in the table, starting with 1, which you want to have in your result
     * @param whereColumnsLocation the columns location in the table (starting with 1), of the condition you want to get AKA column=value
     * @param values the conditions value to the column you want to get from the DB
     * @return ResultSet contains only the {@param selectColumns} of the row where column=value
     * @throws SQLException
     */
    public ResultSet select(Connection connection,List<Integer> selectColumnsLocation, List<Integer> whereColumnsLocation, List<Object> values) throws SQLException {
        validateColumnsNames(selectColumnsLocation);
        validateColumnsNames(whereColumnsLocation);
        String selectColumnsString = selectColumnsLocation.stream().map(this::getColumnName).collect(Collectors.joining(", "));
        return executeQuery(connection,String.format(SELECT_QUERY,selectColumnsString,tableName, makeColumnEqualsQuestionMarkString(whereColumnsLocation,"AND")),values);
    }

    /**
     * gets all the rows which withstands the constraints from {@param column } in {@param value}
     * @param connection a connection to the DB, should be close from the calling function when finish to read the result!
     * @param columnLocation the column location in the db, starting with 1 which are going to be the conditions in the "where" statement.
     * @return ResultSet witch contains the row where column=value
     * @throws SQLException
     */
    public ResultSet getMax(Connection connection, Integer columnLocation) throws SQLException {
        validateColumnsNames(Arrays.asList(columnLocation));
        String selectColumnsString = Arrays.asList(columnLocation).stream().map(this::getColumnName).collect(Collectors.joining(", "));
        return executeQuery(connection,String.format("SELECT max(%s) from %s",selectColumnsString, tableName));
    }

    /**
     *
     * @param values the values the rows u want to add, should be in the db order
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws SQLException
     */
    public int insert(List<Object> values) throws SQLException {
        return executeNonQuery(String.format(INSERT_QUERY,tableName, createQuestionMarkString(values.size())),values);
    }


    /**
     * remove the column with the {@param id}, note that the function works only with tables with id as their first column
     * @param id the columns names in the db, which are going to be the conditions in the "where" statement.
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws SQLException
     */
    public int remove(Object id) throws SQLException {
        return executeNonQuery(String.format(DELETE_QUERY,tableName,tableColumnNames.get(0) +"=?"),Arrays.asList(id));
    }

    /**
     *
     * @param columnsLocation the columns location in the table (starting with 1), which are going to be the conditions in the "where" statement.
     * @param values the conditions value to the column you want to get from the DB
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws SQLException
     */
    public int remove(List<Integer> columnsLocation,List<Object> values) throws SQLException {
        validateColumnsNames(columnsLocation);
        validateColumnValueRatio(columnsLocation,values);
        return executeNonQuery(String.format(DELETE_QUERY,tableName, makeColumnEqualsQuestionMarkString(columnsLocation,"AND")),values);
    }

    /**
     *
     * @param columnsLocationToUpdate columns location in the table (starting with 1), which u want to update
     * @param valuesToUpdate new values u want to update to, must be at the right order of the {@param columnsNamesToUpdate}
     * @param conditionColumnLocation the column location in the table (starting with 1), for the conditions of where u want to update
     * @param conditionValues values u want to be in the condition must be at the right order of the {@param conditionColumnNames}
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws SQLException
     */
    public int update(List<Integer> columnsLocationToUpdate,List<Object> valuesToUpdate,List<Integer> conditionColumnLocation,List<Object> conditionValues) throws SQLException {
        validateColumnsNames(columnsLocationToUpdate);
        validateColumnsNames(conditionColumnLocation);
        validateColumnValueRatio(columnsLocationToUpdate,valuesToUpdate);
        validateColumnValueRatio(conditionColumnLocation,conditionValues);
        return executeNonQuery(String.format(UPDATE_QUERY,tableName, makeColumnEqualsQuestionMarkString(columnsLocationToUpdate,","), makeColumnEqualsQuestionMarkString(conditionColumnLocation,"AND"))
                , Stream.concat(valuesToUpdate.stream(), conditionValues.stream())
                        .collect(Collectors.toList()));
    }

    /**
     * this function create a String in the format "ColumnName1 = ? delimiter ColumnName2 = ? ...)
     * @param columnsNames columns location in the table (starting with 1), to be to join
     * @param delimiter the delimiter to use.
     * @return A String from the format "<columnName> = ? AND <columnName> = ? AND ..."
     */
    private String makeColumnEqualsQuestionMarkString(List<Integer> columnsNames,String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator<Integer> iterator = columnsNames.listIterator();;
        while (iterator.hasNext()){
            builder.append(getColumnName(iterator.next())).append(" = ?");
            if (iterator.hasNext())
                builder.append(String.format(" %s ",delimiter));
        }
        return builder.toString();
    }

    /**
     *  creating a string of '?' delimiter by ','
     * @param length number of '?' you would like to have
     * @return A String from the format "?,? ..."
     */
    private static String createQuestionMarkString(int length) {
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<length;i++)
            builder.append("?, ");
        String output = builder.toString();
        return output.substring(0,output.length()-2);
    }

    /**
     * Replace the '?' in the sql.PreparedStatement execute String
     * @param preparedStatement the sql String executor
     * @param values the value to replace with the '?' in the {@param preparedStatement}
     * @throws SQLException
     */
    private static void replaceQuestionMarks(PreparedStatement preparedStatement, List<Object> values) throws SQLException {
        for(int i =0; i< values.size();i++){
            if (values.get(i)==null) {
                preparedStatement.setNull(i+1, Types.NULL);
            } else if (Integer.class.equals(values.get(i).getClass())) {
                preparedStatement.setInt(i + 1, (Integer) values.get(i));
            } else if (String.class.equals(values.get(i).getClass())) {
                preparedStatement.setString(i + 1, (String) values.get(i));
            } else if (java.util.Date.class.equals(values.get(i).getClass())) {
                java.util.Date javaDate = (java.util.Date) values.get(i);
                preparedStatement.setDate(i + 1, new java.sql.Date(javaDate.getTime()));
            }
            else if(Float.class.equals(values.get(i).getClass())){
                preparedStatement.setFloat(i + 1, (Float) values.get(i));
            }
            else if(Double.class.equals(values.get(i).getClass())){
                preparedStatement.setDouble(i + 1, (Double) values.get(i));
            }
            else if(LocalDate.class.equals(values.get(i).getClass())){
                preparedStatement.setDate(i + 1, java.sql.Date.valueOf((LocalDate) values.get(i)));
            }
            else if (values.get(i).getClass().isEnum()){
                preparedStatement.setString(i + 1, values.get(i).toString());
            }
            else if(Boolean.class.equals(values.get(i).getClass())){
                preparedStatement.setBoolean(i + 1, (Boolean) values.get(i));
            }
            else
                throw new RuntimeException("Did not specified this kind of data");
        }
    }


    /**
     * please remember to close the connection in the end of the use
     * @return A Connection to the table
     * @throws SQLException
     */
    protected ConnectionHandler getConnectionHandler() throws SQLException {
        return new ConnectionHandler();
    }

    private void validateColumnsNames(List<Integer> columnsLocations) {
        for(Integer i: columnsLocations)
            if (i<1 | i> tableColumnNames.size())
                throw new IllegalArgumentException("columns does not exist in the table");
    }


    private void validateColumnValueRatio(List<Integer> columnsNames, List<Object> values) {
        if (columnsNames.size() != values.size())
            throw new IllegalArgumentException("columns and values must be on the same size");
    }

    protected String getColumnName(int index){
        return tableColumnNames.get(index-1);
    }

}

