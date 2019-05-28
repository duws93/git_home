#! /usr/bin/python
#coding:utf-8

import xlsxwriter

filename = '/etc/zabbix/script/test.xlsx'
test_book = xlsxwriter.Workbook(filename)
worksheet = test_book.add_worksheet('test')
bold = test_book.add_format({'bold': True})

test_book.add_format()
expenses = (
    ['Rent', 1000],
    ['Gas',   100],
    ['Food',  300],
    ['Gym',    50],
)

#定义起始的行列会在这个基础上行列各加一作为初始行列


row = 0
col = 0

for item, cost in expenses:
    worksheet.write(row, col, item,bold)
    worksheet.write(row, col+1, cost)
    row += 1

#worksheet.write(row, col, '=sum(B0:B4)')

test_book.close()
