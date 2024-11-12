import 'package:date_field/date_field.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:jiffy/jiffy.dart';

class Screen2 extends StatefulWidget {
  const Screen2({super.key});

  @override
  State<Screen2> createState() => _Screen2State();
}

class _Screen2State extends State<Screen2> {
  DateTime startDate = DateTime.now();
  Jiffy resultDate = Jiffy.now();
  bool add = true;
  int days = 0;
  int months = 0;
  int years = 0;
  String text = "";

  String newDate(
      DateTime startDate, int years, int months, int days, bool add) {
    var date = Jiffy.parseFromDateTime(startDate);
    if (add) {
      date = date.add(days: days, months: months, years: years);
    } else {
      date = date.subtract(days: days, months: months, years: years);
    }
    return date.format(pattern: 'dd/MM/yyyy');
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: SizedBox(
        width: 300,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            DateTimeFormField(
              onChanged: (value) => setState(() {
                if (value != null) {
                  startDate = value;
                }
                text = newDate(startDate, years, months, days, add);
              }),
              mode: DateTimeFieldPickerMode.date,
              dateFormat: DateFormat('dd/MM/yyyy'),
              decoration: const InputDecoration(
                  labelText: "Data początkowa", border: OutlineInputBorder()),
            ),
            const SizedBox(
              height: 15,
            ),
            //TODO: zmienić na listę rozwijaną
            SizedBox(
              width: double.infinity,
              child: SegmentedButton<bool>(
                segments: const <ButtonSegment<bool>>[
                  ButtonSegment<bool>(value: true, label: Text("Dodaj")),
                  ButtonSegment<bool>(value: false, label: Text("Odejmij"))
                ],
                selected: <bool>{add},
                onSelectionChanged: (Set<bool> newSelection) {
                  setState(() {
                    add = newSelection.first;
                    text = newDate(startDate, years, months, days, add);
                  });
                },
              ),
            ),
            const SizedBox(
              height: 15,
            ),
            TextFormField(
              keyboardType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              onChanged: (value) => setState(() {
                if (value.isNotEmpty) {
                  years = int.parse(value);
                } else {
                  years = 0;
                }
                text = newDate(startDate, years, months, days, add);
              }),
              decoration: const InputDecoration(
                  border: OutlineInputBorder(), labelText: "Liczba lat"),
            ),
            const SizedBox(
              height: 15,
            ),
            TextFormField(
              keyboardType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              onChanged: (value) => setState(() {
                if (value.isNotEmpty) {
                  months = int.parse(value);
                } else {
                  months = 0;
                }
                text = newDate(startDate, years, months, days, add);
              }),
              decoration: const InputDecoration(
                  border: OutlineInputBorder(), labelText: "Liczba miesięcy"),
            ),
            const SizedBox(
              height: 15,
            ),
            TextFormField(
              keyboardType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              onChanged: (value) => setState(() {
                if (value.isNotEmpty) {
                  days = int.parse(value);
                } else {
                  days = 0;
                }
                text = newDate(startDate, years, months, days, add);
              }),
              decoration: const InputDecoration(
                  border: OutlineInputBorder(), labelText: "Liczba dni"),
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
            )
          ],
        ),
      ),
    );
  }
}
