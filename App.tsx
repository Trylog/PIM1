import React, { useState } from 'react';
import { View, Text, TextInput, Button, TouchableOpacity, Alert } from 'react-native';
import DatePicker from 'expo-datepicker'; // import expo-datepicker
import { Picker } from '@react-native-picker/picker';
import moment from 'moment';

export default function App() {
  const [selectedTab, setSelectedTab] = useState(0);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [days, setDays] = useState('');
  const [addOrSubtract, setAddOrSubtract] = useState('add');

  const onDateChange = (date, type) => {
    if (type === 'start') {
      setStartDate(moment(date).format('DD/MM/YYYY'));
    } else if (type === 'end') {
      setEndDate(moment(date).format('DD/MM/YYYY'));
    }
  };

  const calculateDateDifference = () => {
    if (startDate && endDate) {
      const start = moment(startDate, 'DD/MM/YYYY');
      const end = moment(endDate, 'DD/MM/YYYY');
      const duration = moment.duration(end.diff(start));

      let result = '';
      if (duration.years() > 0) result += `${duration.years()} years, `;
      if (duration.months() > 0) result += `${duration.months()} months, `;
      result += `${duration.days()} days`;
      return result;
    }
    return '';
  };

  const adjustDays = () => {
    if (startDate && days) {
      const start = moment(startDate, 'DD/MM/YYYY');
      const adjustedDate =
        addOrSubtract === 'add'
          ? start.add(parseInt(days), 'days')
          : start.subtract(parseInt(days), 'days');
      return adjustedDate.format('DD/MM/YYYY');
    }
    return '';
  };

  return (
    <View style={{ flex: 1, padding: 20, justifyContent: 'center', backgroundColor: '#fff' }}>
      {/* Tab Selection */}
      <View style={{ flexDirection: 'row', justifyContent: 'space-around', marginBottom: 20 }}>
        <TouchableOpacity onPress={() => setSelectedTab(0)}>
          <Text style={{ fontSize: 18, color: selectedTab === 0 ? '#65558F' : 'black' }}>Date Difference</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => setSelectedTab(1)}>
          <Text style={{ fontSize: 18, color: selectedTab === 1 ? '#65558F' : 'black' }}>Add/Subtract Days</Text>
        </TouchableOpacity>
      </View>

      {/* Tab Content */}
      {selectedTab === 0 ? (
        <View>
          <Text>Select Start Date:</Text>
          <DatePicker
            date={startDate ? new Date(moment(startDate, 'DD/MM/YYYY').toDate()) : new Date()}
            onDateChange={(date) => onDateChange(date, 'start')}
          />

          <Text>Select End Date:</Text>
          <DatePicker
            date={endDate ? new Date(moment(endDate, 'DD/MM/YYYY').toDate()) : new Date()}
            onDateChange={(date) => onDateChange(date, 'end')}
          />

          <Text style={{ marginTop: 20, fontSize: 16, color: 'black' }}>Difference: {calculateDateDifference()}</Text>
        </View>
      ) : (
        <View>
          <Text>Select Start Date:</Text>
          <DatePicker
            date={startDate ? new Date(moment(startDate, 'DD/MM/YYYY').toDate()) : new Date()}
            onDateChange={(date) => onDateChange(date, 'start')}
          />

          <Text style={{ marginTop: 10 }}>Choose Action:</Text>
          <Picker
            selectedValue={addOrSubtract}
            onValueChange={(itemValue) => setAddOrSubtract(itemValue)}
            style={{ height: 50, width: 150 }}
          >
            <Picker.Item label="Add" value="add" />
            <Picker.Item label="Subtract" value="subtract" />
          </Picker>

          <TextInput
            value={days}
            onChangeText={setDays}
            keyboardType="numeric"
            placeholder="Enter days"
            style={{
              borderWidth: 1,
              borderColor: '#65558F',
              padding: 10,
              marginTop: 10,
              width: 150,
              textAlign: 'center'
            }}
          />

          <Text style={{ marginTop: 20, fontSize: 16, color: 'black' }}>New Date: {adjustDays()}</Text>
        </View>
      )}
    </View>
  );
}
