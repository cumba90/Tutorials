data = spark.read.option('Header', 'True').option('inferSchema', 'True').csv('owid-covid-data.csv')
from pyspark.sql.types import DateType
from pyspark.sql.window import Window
from pyspark.sql.functions import asc, desc, to_date, lit, col, sum, lag

#2

data_2 = data.select('location', to_date('date', 'yyyy-MM-dd').alias('date'), 'new_cases')
data_2.select('location', 'new_cases').filter(~data_2.location.isin(['World', 'Europe', 'European Union', 'Asia', 'South America', 'North America'])).filter(data_2.date >= to_date(lit('2021-03-25'), 'yyyy-MM-dd')).filter(data_2.date <= to_date(lit('2021-03-31'), 'yyyy-MM-dd')).groupBy(data_2.location).agg(sum(data_2.new_cases).alias('sum_new_cases')).orderBy(desc('sum_new_cases')).show(10)
+-------------+-------------+
|     location|sum_new_cases|
+-------------+-------------+
|       Brazil|     528736.0|
|United States|     448300.0|
|        India|     434131.0|
|       France|     266069.0|
|       Turkey|     225900.0|
|       Poland|     201046.0|
|        Italy|     144037.0|
|      Germany|     120656.0|
|      Ukraine|      95016.0|
|       Africa|      81730.0|
+-------------+-------------+
only showing top 10 rows

#3
data_3 = data.select('location', to_date('date', 'yyyy-MM-dd').alias('date'), 'new_cases', lag('new_cases').over(Window.partitionBy('location').orderBy('date')).alias('prev_new_cases')).filter(data.location == lit('Russia'))
data_3.select('location', 'date', 'new_cases', 'prev_new_cases', col('new_cases') - col('prev_new_cases').alias('diff')).filter(data_3.date >= to_date(lit('2021-03-25'), 'yyyy-MM-dd')).filter(data_3.date <= to_date(lit('2021-03-31'), 'yyyy-MM-dd')).show()
+--------+----------+---------+--------------+------------------------------------+
|location|      date|new_cases|prev_new_cases|(new_cases - prev_new_cases AS diff)|
+--------+----------+---------+--------------+------------------------------------+
|  Russia|2021-03-25|   9128.0|        8769.0|                               359.0|
|  Russia|2021-03-26|   9073.0|        9128.0|                               -55.0|
|  Russia|2021-03-27|   8783.0|        9073.0|                              -290.0|
|  Russia|2021-03-28|   8979.0|        8783.0|                               196.0|
|  Russia|2021-03-29|   8589.0|        8979.0|                              -390.0|
|  Russia|2021-03-30|   8162.0|        8589.0|                              -427.0|
|  Russia|2021-03-31|   8156.0|        8162.0|                                -6.0|
+--------+----------+---------+--------------+------------------------------------+