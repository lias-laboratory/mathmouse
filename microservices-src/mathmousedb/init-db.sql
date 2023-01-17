CREATE DATABASE db_mmw;
\connect db_mmw;
CREATE SCHEMA schema_mmw;

CREATE TYPE INTERPOLATION_FUNCTION_TYPE AS ENUM ('none','linear','exponentialSpline','cubicSpline');
CREATE TYPE MATHEMATICAL_OBJECT_TYPE AS ENUM ('binary-operator','unary-operator','function','number','variable');

CREATE TABLE schema_mmw.Group_Name (
	id SERIAL PRIMARY KEY NOT NULL,
	group_name CHAR(50) NOT NULL
);

CREATE TABLE schema_mmw.Differential_Equation (
	id SERIAL PRIMARY KEY NOT NULL,
	eq_order INT NOT NULL CONSTRAINT positive_order CHECK (eq_order > 0),
	name CHAR(150) NOT NULL,
	id_group BIGINT REFERENCES schema_mmw.Group_Name(id) NOT NULL
);

CREATE TABLE schema_mmw.Node_Content (
	id SERIAL PRIMARY KEY NOT NULL CHECK (id > 0),
	math_object MATHEMATICAL_OBJECT_TYPE NOT NULL,
	name CHAR(20) NOT NULL,
	deriv INT NOT NULL CONSTRAINT function_positive_deriv CHECK (deriv > -2)
);

CREATE TABLE schema_mmw.Node (
	id SERIAL PRIMARY KEY NOT NULL CHECK (id > 0),
	id_content BIGINT REFERENCES schema_mmw.Node_Content(id)
);

CREATE TABLE schema_mmw.Node_Connection (
	id SERIAL PRIMARY KEY NOT NULL CHECK (id > 0),
	id_eq BIGINT REFERENCES schema_mmw.Differential_Equation(id) NOT NULL,
	id_node BIGINT REFERENCES schema_mmw.Node(id) NOT NULL,
	id_parent BIGINT REFERENCES schema_mmw.Node(id),
	id_left BIGINT REFERENCES schema_mmw.Node(id),
	id_right BIGINT REFERENCES schema_mmw.Node(id),
	node_depth INT NOT NULL
);

CREATE TABLE schema_mmw.Input_Function (
	id SERIAL PRIMARY KEY NOT NULL CHECK (id > 0),
	name CHAR(20) NOT NULL,
	deriv INT NOT NULL CONSTRAINT input_positive_deriv CHECK (deriv > -2),
	serial_key TEXT NOT NULL,
	interpolation_function INTERPOLATION_FUNCTION_TYPE NOT NULL
);

CREATE TABLE schema_mmw.Equation_Input (
	id SERIAL PRIMARY KEY NOT NULL CHECK (id > 0),
	id_eq BIGINT REFERENCES schema_mmw.Differential_Equation(id),
	id_input_function BIGINT REFERENCES schema_mmw.Input_Function(id)
);

CREATE TABLE schema_mmw.Variable_Value (
	id SERIAL PRIMARY KEY NOT NULL CHECK (id > 0),
	name CHAR(20) NOT NULL,
	var_value REAL NOT NULL
);

CREATE TABLE schema_mmw.Equation_Variable_Value (
	id SERIAL PRIMARY KEY NOT NULL CHECK (id > 0),
	id_eq BIGINT REFERENCES schema_mmw.Differential_Equation(id),
	id_var BIGINT REFERENCES schema_mmw.Variable_Value(id)
);

CREATE TABLE schema_mmw.Initial_Value (
	id SERIAL PRIMARY KEY NOT NULL CHECK (id > 0),
	name CHAR(20) NOT NULL,
	deriv INT NOT NULL CONSTRAINT initial_value_positive_deriv CHECK (deriv > -2),
	init_value REAL NOT NULL
);

CREATE TABLE schema_mmw.Equation_Initial_Value (
	id SERIAL PRIMARY KEY NOT NULL CHECK (id > 0),
	id_eq BIGINT REFERENCES schema_mmw.Differential_Equation(id),
	id_initial BIGINT REFERENCES schema_mmw.Initial_Value(id)
);
