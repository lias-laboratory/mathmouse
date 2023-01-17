-- list of the nodes representing the equations stored in the database
SELECT schema_mmw.Differential_Equation.id AS id_eq,schema_mmw.Node.id AS id_node,schema_mmw.Node_Connection.node_depth,schema_mmw.Node_Content.math_object,schema_mmw.Node_Content.name,schema_mmw.Node_Content.deriv,schema_mmw.Node_Connection.id_parent,schema_mmw.Node_Connection.id_left,schema_mmw.Node_Connection.id_right
FROM schema_mmw.Differential_Equation,schema_mmw.Node,schema_mmw.Node_Content,schema_mmw.Node_Connection
WHERE schema_mmw.Differential_Equation.id = schema_mmw.Node_Connection.id_eq
AND schema_mmw.Node_Connection.id_node = schema_mmw.Node.id
AND schema_mmw.Node.id_content = schema_mmw.Node_Content.id;

-- list of the variables for each equations
SELECT schema_mmw.Differential_Equation.id AS id_eq,schema_mmw.Variable_Value.id AS id_var,schema_mmw.Variable_Value.name,schema_mmw.Variable_Value.var_value
FROM schema_mmw.Variable_Value,schema_mmw.Equation_Variable_Value,schema_mmw.Differential_Equation
WHERE schema_mmw.Differential_Equation.id = schema_mmw.Equation_Variable_Value.id_eq
AND schema_mmw.Variable_Value.id = schema_mmw.Equation_Variable_Value.id_var;

-- list of the input functions of each equations
SELECT schema_mmw.Differential_Equation.id AS id_eq,schema_mmw.Input_Function.id AS id_input_function,schema_mmw.Input_Function.name,schema_mmw.Input_Function.deriv,schema_mmw.Input_Function.file_path
FROM schema_mmw.Input_Function,schema_mmw.Equation_Input,schema_mmw.Differential_Equation
WHERE schema_mmw.Input_Function.id = schema_mmw.Equation_Input.id_input_function
AND schema_mmw.Differential_Equation.id = schema_mmw.Equation_Input.id_eq;

-- list of the intial values of each equations
SELECT schema_mmw.Initial_Value.id AS id_initial,schema_mmw.Differential_Equation.id AS id_eq,schema_mmw.Initial_Value.name,schema_mmw.Initial_Value.deriv,schema_mmw.Initial_Value.init_value
FROM schema_mmw.Initial_Value,schema_mmw.Equation_Initial_Value,schema_mmw.Differential_Equation
WHERE schema_mmw.Initial_Value.id = schema_mmw.Equation_Initial_Value.id_initial
AND schema_mmw.Differential_Equation.id = schema_mmw.Equation_Initial_Value.id_eq;

SELECT * FROM schema_mmw.Differential_Equation ORDER BY id;

SELECT schema_mmw.Node.id FROM schema_mmw.Node ORDER BY id;

SELECT schema_mmw.Input_Function.id FROM schema_mmw.Input_Function ORDER BY id;

SELECT schema_mmw.Variable_Value.id FROM schema_mmw.Variable_Value ORDER BY id;

SELECT schema_mmw.Initial_Value.id FROM schema_mmw.Initial_Value ORDER BY id;

SELECT * FROM schema_mmw.Node_Content ORDER BY id;

SELECT * FROM schema_mmw.Node ORDER BY id;

SELECT * FROM schema_mmw.Node_Connection ORDER BY id;

-- selection of equations containing some particular variables (m)
SELECT schema_mmw.Differential_Equation.id 
FROM schema_mmw.Differential_Equation,schema_mmw.Variable_Value,schema_mmw.Equation_Variable_Value 
WHERE schema_mmw.Differential_Equation.id = schema_mmw.Equation_Variable_Value.id_eq
AND schema_mmw.Variable_Value.id = schema_mmw.Equation_Variable_Value.id_var
AND schema_mmw.Variable_Value.name = 'm';

-- selection of all initial conditions of a given equations
SELECT schema_mmw.Initial_Value.name,schema_mmw.Initial_Value.deriv,schema_mmw.Initial_Value.init_value 
FROM schema_mmw.Initial_Value,schema_mmw.Equation_Initial_Value 
WHERE schema_mmw.Equation_Initial_Value.id = 3;

-- selection of all the equations of a given order
SELECT * FROM schema_mmw.Differential_Equation WHERE eq_order = 1;

--selection of equations for variables defined in particular ranges
SELECT schema_mmw.Differential_Equation.id 
FROM schema_mmw.Differential_Equation,schema_mmw.Variable_Value,schema_mmw.Equation_Variable_Value 
WHERE schema_mmw.Differential_Equation.id = schema_mmw.Equation_Variable_Value.id_eq
AND schema_mmw.Variable_Value.id = schema_mmw.Equation_Variable_Value.id_var
AND schema_mmw.Variable_Value.name = 'T'
AND schema_mmw.Variable_Value.var_value > 10.0
AND schema_mmw.Variable_Value.var_value < 30.0;