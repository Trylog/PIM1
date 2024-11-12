import 'package:flutter/material.dart';
import 'package:date_field/date_field.dart';
import 'package:intl/intl.dart';
import 'package:jiffy/jiffy.dart';

class Screen1 extends StatefulWidget {
  const Screen1({super.key});

  @override
  State<Screen1> createState() => _Screen1State();
}

class _Screen1State extends State<Screen1> {
  DateTime startDate = DateTime.now();
  DateTime endDate = DateTime.now();
  int days = 0;
  String text = "";

  int daysBetween(DateTime from, DateTime to) {
    from = DateTime(from.year, from.month, from.day);
    to = DateTime(to.year, to.month, to.day);
    return (to.difference(from).inHours / 24).round();
  }

  String difference(DateTime from, DateTime to) {
    var start = Jiffy.parseFromDateTime(from);
    var end = Jiffy.parseFromDateTime(to);
    var years = end.diff(start, unit: Unit.year).toInt();
    var months = end.diff(start.add(years: years), unit: Unit.month).toInt();
    var monthsFull = end.diff(start, unit: Unit.month);
    var days =
        end.diff(start.add(years: years, months: months), unit: Unit.day);
    var daysFull = end.diff(start, unit: Unit.day);
    String result = "";
    if (years == 1) {
      result += "1 rok";
    } else if (years >= 2) {
      if ((years / 10).floor() != 1 && years % 10 >= 2 && years % 10 < 5) {
        result += "$years lata";
      } else {
        result += "$years lat";
      }
    }
    if (months == 1) {
      result += ", 1 miesiąc";
    } else if (months >= 2 && months < 5) {
      result += ", $months miesiące";
    } else if (months >= 5) {
      result += ", $months miesięcy";
    }
    if (years > 0 || months > 0) {
      if (days == 1) {
        result += ", 1 dzień";
      } else if (days >= 2) {
        result += ", $days dni";
      }
      result += "\n";
    }
    if (years >= 1) {
      if ((monthsFull / 10).floor() != 1 &&
          monthsFull % 10 >= 2 &&
          monthsFull % 10 < 5) {
        result += "$monthsFull miesiące lub ";
      } else {
        result += "$monthsFull miesięcy lub ";
      }
    }
    if (daysFull == 1) {
      result += "1 dzień";
    } else if (daysFull >= 2) {
      result += "$daysFull dni";
    }
    return result;
  }

  @override
  Widget build(BuildContext context) {
    return Center(
        child: SizedBox(
      width: 300,
      child: Column(mainAxisAlignment: MainAxisAlignment.center, children: [
        Align(
          alignment: Alignment.bottomCenter,
          child: DateTimeFormField(
            onChanged: (value) => setState(() {
              if (value != null) {
                startDate = value;
              }
              days = daysBetween(startDate, endDate);
              text = difference(startDate, endDate);
            }),
            mode: DateTimeFieldPickerMode.date,
            dateFormat: DateFormat('dd/MM/yyyy'),
            decoration: const InputDecoration(
                labelText: "Data początkowa", border: OutlineInputBorder()),
          ),
        ),
        const SizedBox(
          height: 15,
        ),
        DateTimeFormField(
          onChanged: (value) => setState(() {
            if (value != null) {
              endDate = value;
            }
            days = daysBetween(startDate, endDate);
            text = difference(startDate, endDate);
          }),
          mode: DateTimeFieldPickerMode.date,
          dateFormat: DateFormat('dd/MM/yyyy'),
          decoration: const InputDecoration(
              labelText: "Data końcowa", border: OutlineInputBorder()),
        ),
        const SizedBox(
          height: 15,
        ),
        SizedBox(
          width: double.infinity,
          height: 56,
          child: Card.filled(
            child: Align(child: Text(text)),
          ),
        ),
      ]),
    ));
  }
}
