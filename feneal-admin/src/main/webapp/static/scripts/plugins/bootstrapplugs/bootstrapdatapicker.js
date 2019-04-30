!function(a, b) {
    "use strict";
    if ("function" == typeof define && define.amd)
        define(["jquery", "moment"], b);
    else if ("object" == typeof exports)
        b(require("jquery"), require("moment"));
    else {
        if (!jQuery)
            throw new Error("bootstrap-datetimepicker requires jQuery to be loaded first");
        if (!moment)
            throw new Error("bootstrap-datetimepicker requires moment.js to be loaded first");
        b(a.jQuery, moment)
    }
}(this, function(a, b) {
    "use strict";
    if ("undefined" == typeof b)
        throw new Error("momentjs is required");
    var c = 0
        , d = function(d, e) {
        var f, g = a.fn.datetimepicker.defaults, h = {
            time: "glyphicon glyphicon-time",
            date: "glyphicon glyphicon-calendar",
            up: "glyphicon glyphicon-chevron-up",
            down: "glyphicon glyphicon-chevron-down"
        }, i = this, j = !1, k = function() {
            var f, j, k = !1;
            if (i.options = a.extend({}, g, e),
                    i.options.icons = a.extend({}, h, i.options.icons),
                    i.element = a(d),
                    m(),
                !i.options.pickTime && !i.options.pickDate)
                throw new Error("Must choose at least one picker");
            if (i.id = c++,
                    b.locale(i.options.language),
                    i.date = b(),
                    i.unset = !1,
                    i.isInput = i.element.is("input"),
                    i.component = !1,
                i.element.hasClass("input-group") && (i.component = i.element.find(0 === i.element.find(".datepickerbutton").length ? '[class^="input-group-"]' : ".datepickerbutton")),
                    i.format = i.options.format,
                    f = b().localeData(),
                i.format || (i.format = i.options.pickDate ? f.longDateFormat("L") : "",
                i.options.pickDate && i.options.pickTime && (i.format += " "),
                    i.format += i.options.pickTime ? f.longDateFormat("LT") : "",
                i.options.useSeconds && (-1 !== f.longDateFormat("LT").indexOf(" A") ? i.format = i.format.split(" A")[0] + ":ss A" : i.format += ":ss")),
                    i.use24hours = i.format.toLowerCase().indexOf("a") < 0 && i.format.indexOf("h") < 0,
                i.component && (k = i.component.find("span")),
                i.options.pickTime && k && k.addClass(i.options.icons.time),
                i.options.pickDate && k && (k.removeClass(i.options.icons.time),
                    k.addClass(i.options.icons.date)),
                    i.options.widgetParent = "string" == typeof i.options.widgetParent && i.options.widgetParent || i.element.parents().filter(function() {
                        return "scroll" === a(this).css("overflow-y")
                    }).get(0) || "body",
                    i.widget = a(Q()).appendTo(i.options.widgetParent),
                    i.minViewMode = i.options.minViewMode || 0,
                "string" == typeof i.minViewMode)
                switch (i.minViewMode) {
                    case "months":
                        i.minViewMode = 1;
                        break;
                    case "years":
                        i.minViewMode = 2;
                        break;
                    default:
                        i.minViewMode = 0
                }
            if (i.viewMode = i.options.viewMode || 0,
                "string" == typeof i.viewMode)
                switch (i.viewMode) {
                    case "months":
                        i.viewMode = 1;
                        break;
                    case "years":
                        i.viewMode = 2;
                        break;
                    default:
                        i.viewMode = 0
                }
            i.viewMode = Math.max(i.viewMode, i.minViewMode),
                i.options.disabledDates = O(i.options.disabledDates),
                i.options.enabledDates = O(i.options.enabledDates),
                i.startViewMode = i.viewMode,
                i.setMinDate(i.options.minDate),
                i.setMaxDate(i.options.maxDate),
                r(),
                s(),
                u(),
                v(),
                w(),
                q(),
                E(),
            l().prop("disabled") || F(),
            "" !== i.options.defaultDate && "" === l().val() && i.setValue(i.options.defaultDate),
            1 !== i.options.minuteStepping && (j = i.options.minuteStepping,
                i.date.minutes(Math.round(i.date.minutes() / j) * j % 60).seconds(0))
        }, l = function() {
            var a;
            if (i.isInput)
                return i.element;
            if (a = i.element.find(".datepickerinput"),
                0 === a.length)
                a = i.element.find("input");
            else if (!a.is("input"))
                throw new Error('CSS class "datepickerinput" cannot be applied to non input element');
            return a
        }, m = function() {
            var a;
            a = i.element.is("input") ? i.element.data() : i.element.find("input").data(),
            void 0 !== a.dateFormat && (i.options.format = a.dateFormat),
            void 0 !== a.datePickdate && (i.options.pickDate = a.datePickdate),
            void 0 !== a.datePicktime && (i.options.pickTime = a.datePicktime),
            void 0 !== a.dateUseminutes && (i.options.useMinutes = a.dateUseminutes),
            void 0 !== a.dateUseseconds && (i.options.useSeconds = a.dateUseseconds),
            void 0 !== a.dateUsecurrent && (i.options.useCurrent = a.dateUsecurrent),
            void 0 !== a.calendarWeeks && (i.options.calendarWeeks = a.calendarWeeks),
            void 0 !== a.dateMinutestepping && (i.options.minuteStepping = a.dateMinutestepping),
            void 0 !== a.dateMindate && (i.options.minDate = a.dateMindate),
            void 0 !== a.dateMaxdate && (i.options.maxDate = a.dateMaxdate),
            void 0 !== a.dateShowtoday && (i.options.showToday = a.dateShowtoday),
            void 0 !== a.dateCollapse && (i.options.collapse = a.dateCollapse),
            void 0 !== a.dateLanguage && (i.options.language = a.dateLanguage),
            void 0 !== a.dateDefaultdate && (i.options.defaultDate = a.dateDefaultdate),
            void 0 !== a.dateDisableddates && (i.options.disabledDates = a.dateDisableddates),
            void 0 !== a.dateEnableddates && (i.options.enabledDates = a.dateEnableddates),
            void 0 !== a.dateIcons && (i.options.icons = a.dateIcons),
            void 0 !== a.dateUsestrict && (i.options.useStrict = a.dateUsestrict),
            void 0 !== a.dateDirection && (i.options.direction = a.dateDirection),
            void 0 !== a.dateSidebyside && (i.options.sideBySide = a.dateSidebyside),
            void 0 !== a.dateDaysofweekdisabled && (i.options.daysOfWeekDisabled = a.dateDaysofweekdisabled)
        }, n = function() {
            var b, c = "absolute", d = i.component ? i.component.offset() : i.element.offset(), e = a(window);
            i.width = i.component ? i.component.outerWidth() : i.element.outerWidth(),
                d.top = d.top + i.element.outerHeight(),
                "up" === i.options.direction ? b = "top" : "bottom" === i.options.direction ? b = "bottom" : "auto" === i.options.direction && (b = d.top + i.widget.height() > e.height() + e.scrollTop() && i.widget.height() + i.element.outerHeight() < d.top ? "top" : "bottom"),
                "top" === b ? (d.bottom = e.height() - d.top + i.element.outerHeight() + 3,
                    i.widget.addClass("top").removeClass("bottom")) : (d.top += 1,
                    i.widget.addClass("bottom").removeClass("top")),
            void 0 !== i.options.width && i.widget.width(i.options.width),
            "left" === i.options.orientation && (i.widget.addClass("left-oriented"),
                d.left = d.left - i.widget.width() + 20),
            J() && (c = "fixed",
                d.top -= e.scrollTop(),
                d.left -= e.scrollLeft()),
                e.width() < d.left + i.widget.outerWidth() ? (d.right = e.width() - d.left - i.width,
                    d.left = "auto",
                    i.widget.addClass("pull-right")) : (d.right = "auto",
                    i.widget.removeClass("pull-right")),
                i.widget.css("top" === b ? {
                    position: c,
                    bottom: d.bottom,
                    top: "auto",
                    left: d.left,
                    right: d.right
                } : {
                    position: c,
                    top: d.top,
                    bottom: "auto",
                    left: d.left,
                    right: d.right
                })
        }, o = function(a, c) {
            (!b(i.date).isSame(b(a)) || j) && (j = !1,
                i.element.trigger({
                    type: "dp.change",
                    date: b(i.date),
                    oldDate: b(a)
                }),
            "change" !== c && i.element.change())
        }, p = function(a) {
            j = !0,
                i.element.trigger({
                    type: "dp.error",
                    date: b(a, i.format, i.options.useStrict)
                })
        }, q = function(a) {
            b.locale(i.options.language);
            var c = a;
            c || (c = l().val(),
            c && (i.date = b(c, i.format, i.options.useStrict)),
            i.date || (i.date = b())),
                i.viewDate = b(i.date).startOf("month"),
                t(),
                x()
        }, r = function() {
            b.locale(i.options.language);
            var c, d = a("<tr>"), e = b.weekdaysMin();
            if (i.options.calendarWeeks === !0 && d.append('<th class="cw">#</th>'),
                0 === b().localeData()._week.dow)
                for (c = 0; 7 > c; c++)
                    d.append('<th class="dow">' + e[c] + "</th>");
            else
                for (c = 1; 8 > c; c++)
                    d.append(7 === c ? '<th class="dow">' + e[0] + "</th>" : '<th class="dow">' + e[c] + "</th>");
            i.widget.find(".datepicker-days thead").append(d)
        }, s = function() {
            b.locale(i.options.language);
            var a, c = "", d = b.monthsShort();
            for (a = 0; 12 > a; a++)
                c += '<span class="month">' + d[a] + "</span>";
            i.widget.find(".datepicker-months td").append(c)
        }, t = function() {
            if (i.options.pickDate) {
                b.locale(i.options.language);
                var c, d, e, f, g, h, j, k, l, m = i.viewDate.year(), n = i.viewDate.month(), o = i.options.minDate.year(), p = i.options.minDate.month(), q = i.options.maxDate.year(), r = i.options.maxDate.month(), s = [], t = b.months();
                for (i.widget.find(".datepicker-days").find(".disabled").removeClass("disabled"),
                         i.widget.find(".datepicker-months").find(".disabled").removeClass("disabled"),
                         i.widget.find(".datepicker-years").find(".disabled").removeClass("disabled"),
                         i.widget.find(".datepicker-days th:eq(1)").text(t[n] + " " + m),
                         d = b(i.viewDate, i.format, i.options.useStrict).subtract(1, "months"),
                         j = d.daysInMonth(),
                         d.date(j).startOf("week"),
                     (m === o && p >= n || o > m) && i.widget.find(".datepicker-days th:eq(0)").addClass("disabled"),
                     (m === q && n >= r || m > q) && i.widget.find(".datepicker-days th:eq(2)").addClass("disabled"),
                         e = b(d).add(42, "d"); d.isBefore(e); ) {
                    if (d.weekday() === b().startOf("week").weekday() && (f = a("<tr>"),
                            s.push(f),
                        i.options.calendarWeeks === !0 && f.append('<td class="cw">' + d.week() + "</td>")),
                            g = "",
                            d.year() < m || d.year() === m && d.month() < n ? g += " old" : (d.year() > m || d.year() === m && d.month() > n) && (g += " new"),
                        d.isSame(b({
                            y: i.date.year(),
                            M: i.date.month(),
                            d: i.date.date()
                        })) && (g += " active"),
                        (M(d, "day") || !N(d)) && (g += " disabled"),
                        i.options.showToday === !0 && d.isSame(b(), "day") && (g += " today"),
                            i.options.daysOfWeekDisabled)
                        for (h = 0; h < i.options.daysOfWeekDisabled.length; h++)
                            if (d.day() === i.options.daysOfWeekDisabled[h]) {
                                g += " disabled";
                                break
                            }
                    f.append('<td class="day' + g + '">' + d.date() + "</td>"),
                        c = d.date(),
                        d.add(1, "d"),
                    c === d.date() && d.add(1, "d")
                }
                for (i.widget.find(".datepicker-days tbody").empty().append(s),
                         l = i.date.year(),
                         t = i.widget.find(".datepicker-months").find("th:eq(1)").text(m).end().find("span").removeClass("active"),
                     l === m && t.eq(i.date.month()).addClass("active"),
                     o > m - 1 && i.widget.find(".datepicker-months th:eq(0)").addClass("disabled"),
                     m + 1 > q && i.widget.find(".datepicker-months th:eq(2)").addClass("disabled"),
                         h = 0; 12 > h; h++)
                    m === o && p > h || o > m ? a(t[h]).addClass("disabled") : (m === q && h > r || m > q) && a(t[h]).addClass("disabled");
                for (s = "",
                         m = 10 * parseInt(m / 10, 10),
                         k = i.widget.find(".datepicker-years").find("th:eq(1)").text(m + "-" + (m + 9)).parents("table").find("td"),
                         i.widget.find(".datepicker-years").find("th").removeClass("disabled"),
                     o > m && i.widget.find(".datepicker-years").find("th:eq(0)").addClass("disabled"),
                     m + 9 > q && i.widget.find(".datepicker-years").find("th:eq(2)").addClass("disabled"),
                         m -= 1,
                         h = -1; 11 > h; h++)
                    s += '<span class="year' + (-1 === h || 10 === h ? " old" : "") + (l === m ? " active" : "") + (o > m || m > q ? " disabled" : "") + '">' + m + "</span>",
                        m += 1;
                k.html(s)
            }
        }, u = function() {
            b.locale(i.options.language);
            var a, c, d, e = i.widget.find(".timepicker .timepicker-hours table"), f = "";
            if (e.parent().hide(),
                    i.use24hours)
                for (a = 0,
                         c = 0; 6 > c; c += 1) {
                    for (f += "<tr>",
                             d = 0; 4 > d; d += 1)
                        f += '<td class="hour">' + P(a.toString()) + "</td>",
                            a++;
                    f += "</tr>"
                }
            else
                for (a = 1,
                         c = 0; 3 > c; c += 1) {
                    for (f += "<tr>",
                             d = 0; 4 > d; d += 1)
                        f += '<td class="hour">' + P(a.toString()) + "</td>",
                            a++;
                    f += "</tr>"
                }
            e.html(f)
        }, v = function() {
            var a, b, c = i.widget.find(".timepicker .timepicker-minutes table"), d = "", e = 0, f = i.options.minuteStepping;
            for (c.parent().hide(),
                 1 === f && (f = 5),
                     a = 0; a < Math.ceil(60 / f / 4); a++) {
                for (d += "<tr>",
                         b = 0; 4 > b; b += 1)
                    60 > e ? (d += '<td class="minute">' + P(e.toString()) + "</td>",
                        e += f) : d += "<td></td>";
                d += "</tr>"
            }
            c.html(d)
        }, w = function() {
            var a, b, c = i.widget.find(".timepicker .timepicker-seconds table"), d = "", e = 0;
            for (c.parent().hide(),
                     a = 0; 3 > a; a++) {
                for (d += "<tr>",
                         b = 0; 4 > b; b += 1)
                    d += '<td class="second">' + P(e.toString()) + "</td>",
                        e += 5;
                d += "</tr>"
            }
            c.html(d)
        }, x = function() {
            if (i.date) {
                var a = i.widget.find(".timepicker span[data-time-component]")
                    , b = i.date.hours()
                    , c = i.date.format("A");
                i.use24hours || (0 === b ? b = 12 : 12 !== b && (b %= 12),
                    i.widget.find(".timepicker [data-action=togglePeriod]").text(c)),
                    a.filter("[data-time-component=hours]").text(P(b)),
                    a.filter("[data-time-component=minutes]").text(P(i.date.minutes())),
                    a.filter("[data-time-component=seconds]").text(P(i.date.second()))
            }
        }, y = function(c) {
            c.stopPropagation(),
                c.preventDefault(),
                i.unset = !1;
            var d, e, f, g, h = a(c.target).closest("span, td, th"), j = b(i.date);
            if (1 === h.length && !h.is(".disabled"))
                switch (h[0].nodeName.toLowerCase()) {
                    case "th":
                        switch (h[0].className) {
                            case "picker-switch":
                                E(1);
                                break;
                            case "prev":
                            case "next":
                                f = R.modes[i.viewMode].navStep,
                                "prev" === h[0].className && (f = -1 * f),
                                    i.viewDate.add(f, R.modes[i.viewMode].navFnc),
                                    t()
                        }
                        break;
                    case "span":
                        h.is(".month") ? (d = h.parent().find("span").index(h),
                            i.viewDate.month(d)) : (e = parseInt(h.text(), 10) || 0,
                            i.viewDate.year(e)),
                        i.viewMode === i.minViewMode && (i.date = b({
                            y: i.viewDate.year(),
                            M: i.viewDate.month(),
                            d: i.viewDate.date(),
                            h: i.date.hours(),
                            m: i.date.minutes(),
                            s: i.date.seconds()
                        }),
                            K(),
                            o(j, c.type)),
                            E(-1),
                            t();
                        break;
                    case "td":
                        h.is(".day") && (g = parseInt(h.text(), 10) || 1,
                            d = i.viewDate.month(),
                            e = i.viewDate.year(),
                            h.is(".old") ? 0 === d ? (d = 11,
                                e -= 1) : d -= 1 : h.is(".new") && (11 === d ? (d = 0,
                                e += 1) : d += 1),
                            i.date = b({
                                y: e,
                                M: d,
                                d: g,
                                h: i.date.hours(),
                                m: i.date.minutes(),
                                s: i.date.seconds()
                            }),
                            i.viewDate = b({
                                y: e,
                                M: d,
                                d: Math.min(28, g)
                            }),
                            t(),
                            K(),
                            o(j, c.type))
                }
        }, z = {
            incrementHours: function() {
                L("add", "hours", 1)
            },
            incrementMinutes: function() {
                L("add", "minutes", i.options.minuteStepping)
            },
            incrementSeconds: function() {
                L("add", "seconds", 1)
            },
            decrementHours: function() {
                L("subtract", "hours", 1)
            },
            decrementMinutes: function() {
                L("subtract", "minutes", i.options.minuteStepping)
            },
            decrementSeconds: function() {
                L("subtract", "seconds", 1)
            },
            togglePeriod: function() {
                var a = i.date.hours();
                a >= 12 ? a -= 12 : a += 12,
                    i.date.hours(a)
            },
            showPicker: function() {
                i.widget.find(".timepicker > div:not(.timepicker-picker)").hide(),
                    i.widget.find(".timepicker .timepicker-picker").show()
            },
            showHours: function() {
                i.widget.find(".timepicker .timepicker-picker").hide(),
                    i.widget.find(".timepicker .timepicker-hours").show()
            },
            showMinutes: function() {
                i.widget.find(".timepicker .timepicker-picker").hide(),
                    i.widget.find(".timepicker .timepicker-minutes").show()
            },
            showSeconds: function() {
                i.widget.find(".timepicker .timepicker-picker").hide(),
                    i.widget.find(".timepicker .timepicker-seconds").show()
            },
            selectHour: function(b) {
                var c = parseInt(a(b.target).text(), 10);
                i.use24hours || (i.date.hours() >= 12 ? 12 !== c && (c += 12) : 12 === c && (c = 0)),
                    i.date.hours(c),
                    z.showPicker.call(i)
            },
            selectMinute: function(b) {
                i.date.minutes(parseInt(a(b.target).text(), 10)),
                    z.showPicker.call(i)
            },
            selectSecond: function(b) {
                i.date.seconds(parseInt(a(b.target).text(), 10)),
                    z.showPicker.call(i)
            }
        }, A = function(c) {
            var d = b(i.date)
                , e = a(c.currentTarget).data("action")
                , f = z[e].apply(i, arguments);
            return B(c),
            i.date || (i.date = b({
                y: 1970
            })),
                K(),
                x(),
                o(d, c.type),
                f
        }, B = function(a) {
            a.stopPropagation(),
                a.preventDefault()
        }, C = function(a) {
            27 === a.keyCode && i.hide()
        }, D = function(c) {
            b.locale(i.options.language);
            var d = a(c.target)
                , e = b(i.date)
                , f = b(d.val(), i.format, i.options.useStrict);
            f.isValid() && !M(f) && N(f) ? (q(),
                i.setValue(f),
                o(e, c.type),
                K()) : (i.viewDate = e,
                i.unset = !0,
                o(e, c.type),
                p(f))
        }, E = function(a) {
            a && (i.viewMode = Math.max(i.minViewMode, Math.min(2, i.viewMode + a))),
                i.widget.find(".datepicker > div").hide().filter(".datepicker-" + R.modes[i.viewMode].clsName).show()
        }, F = function() {
            var b, c, d, e, f;
            i.widget.on("click", ".datepicker *", a.proxy(y, this)),
                i.widget.on("click", "[data-action]", a.proxy(A, this)),
                i.widget.on("mousedown", a.proxy(B, this)),
                i.element.on("keydown", a.proxy(C, this)),
            i.options.pickDate && i.options.pickTime && i.widget.on("click.togglePicker", ".accordion-toggle", function(g) {
                if (g.stopPropagation(),
                        b = a(this),
                        c = b.closest("ul"),
                        d = c.find(".in"),
                        e = c.find(".collapse:not(.in)"),
                    d && d.length) {
                    if (f = d.data("collapse"),
                        f && f.transitioning)
                        return;
                    d.collapse("hide"),
                        e.collapse("show"),
                        b.find("span").toggleClass(i.options.icons.time + " " + i.options.icons.date),
                    i.component && i.component.find("span").toggleClass(i.options.icons.time + " " + i.options.icons.date)
                }
            }),
                i.isInput ? i.element.on({
                    click: a.proxy(i.show, this),
                    focus: a.proxy(i.show, this),
                    change: a.proxy(D, this),
                    blur: a.proxy(i.hide, this)
                }) : (i.element.on({
                    change: a.proxy(D, this)
                }, "input"),
                    i.component ? (i.component.on("click", a.proxy(i.show, this)),
                        i.component.on("mousedown", a.proxy(B, this))) : i.element.on("click", a.proxy(i.show, this)))
        }, G = function() {
            a(window).on("resize.datetimepicker" + i.id, a.proxy(n, this)),
            i.isInput || a(document).on("mousedown.datetimepicker" + i.id, a.proxy(i.hide, this))
        }, H = function() {
            i.widget.off("click", ".datepicker *", i.click),
                i.widget.off("click", "[data-action]"),
                i.widget.off("mousedown", i.stopEvent),
            i.options.pickDate && i.options.pickTime && i.widget.off("click.togglePicker"),
                i.isInput ? i.element.off({
                    focus: i.show,
                    change: D,
                    click: i.show,
                    blur: i.hide
                }) : (i.element.off({
                    change: D
                }, "input"),
                    i.component ? (i.component.off("click", i.show),
                        i.component.off("mousedown", i.stopEvent)) : i.element.off("click", i.show))
        }, I = function() {
            a(window).off("resize.datetimepicker" + i.id),
            i.isInput || a(document).off("mousedown.datetimepicker" + i.id)
        }, J = function() {
            if (i.element) {
                var b, c = i.element.parents(), d = !1;
                for (b = 0; b < c.length; b++)
                    if ("fixed" === a(c[b]).css("position")) {
                        d = !0;
                        break
                    }
                return d
            }
            return !1
        }, K = function() {
            b.locale(i.options.language);
            var a = "";
            i.unset || (a = b(i.date).format(i.format)),
                l().val(a),
                i.element.data("date", a),
            i.options.pickTime || i.hide()
        }, L = function(a, c, d) {
            b.locale(i.options.language);
            var e;
            return "add" === a ? (e = b(i.date),
            23 === e.hours() && e.add(d, c),
                e.add(d, c)) : e = b(i.date).subtract(d, c),
                M(b(e.subtract(d, c))) || M(e) ? void p(e.format(i.format)) : ("add" === a ? i.date.add(d, c) : i.date.subtract(d, c),
                    void (i.unset = !1))
        }, M = function(a, c) {
            b.locale(i.options.language);
            var d = b(i.options.maxDate, i.format, i.options.useStrict)
                , e = b(i.options.minDate, i.format, i.options.useStrict);
            return c && (d = d.endOf(c),
                e = e.startOf(c)),
                a.isAfter(d) || a.isBefore(e) ? !0 : i.options.disabledDates === !1 ? !1 : i.options.disabledDates[a.format("YYYY-MM-DD")] === !0
        }, N = function(a) {
            return b.locale(i.options.language),
                i.options.enabledDates === !1 ? !0 : i.options.enabledDates[a.format("YYYY-MM-DD")] === !0
        }, O = function(a) {
            var c, d = {}, e = 0;
            for (c = 0; c < a.length; c++)
                f = b.isMoment(a[c]) || a[c]instanceof Date ? b(a[c]) : b(a[c], i.format, i.options.useStrict),
                f.isValid() && (d[f.format("YYYY-MM-DD")] = !0,
                    e++);
            return e > 0 ? d : !1
        }, P = function(a) {
            return a = a.toString(),
                a.length >= 2 ? a : "0" + a
        }, Q = function() {
            var a = '<thead><tr><th class="prev">&lsaquo;</th><th colspan="' + (i.options.calendarWeeks ? "6" : "5") + '" class="picker-switch"></th><th class="next">&rsaquo;</th></tr></thead>'
                , b = '<tbody><tr><td colspan="' + (i.options.calendarWeeks ? "8" : "7") + '"></td></tr></tbody>'
                , c = '<div class="datepicker-days"><table class="table-condensed">' + a + '<tbody></tbody></table></div><div class="datepicker-months"><table class="table-condensed">' + a + b + '</table></div><div class="datepicker-years"><table class="table-condensed">' + a + b + "</table></div>"
                , d = "";
            return i.options.pickDate && i.options.pickTime ? (d = '<div class="bootstrap-datetimepicker-widget' + (i.options.sideBySide ? " timepicker-sbs" : "") + (i.use24hours ? " usetwentyfour" : "") + ' dropdown-menu" style="z-index:9999 !important;">',
                d += i.options.sideBySide ? '<div class="row"><div class="col-sm-6 datepicker">' + c + '</div><div class="col-sm-6 timepicker">' + S.getTemplate() + "</div></div>" : '<ul class="list-unstyled"><li' + (i.options.collapse ? ' class="collapse in"' : "") + '><div class="datepicker">' + c + '</div></li><li class="picker-switch accordion-toggle"><a class="btn" style="width:100%"><span class="' + i.options.icons.time + '"></span></a></li><li' + (i.options.collapse ? ' class="collapse"' : "") + '><div class="timepicker">' + S.getTemplate() + "</div></li></ul>",
                d += "</div>") : i.options.pickTime ? '<div class="bootstrap-datetimepicker-widget dropdown-menu"><div class="timepicker">' + S.getTemplate() + "</div></div>" : '<div class="bootstrap-datetimepicker-widget dropdown-menu"><div class="datepicker">' + c + "</div></div>"
        }, R = {
            modes: [{
                clsName: "days",
                navFnc: "month",
                navStep: 1
            }, {
                clsName: "months",
                navFnc: "year",
                navStep: 1
            }, {
                clsName: "years",
                navFnc: "year",
                navStep: 10
            }]
        }, S = {
            hourTemplate: '<span data-action="showHours"   data-time-component="hours"   class="timepicker-hour"></span>',
            minuteTemplate: '<span data-action="showMinutes" data-time-component="minutes" class="timepicker-minute"></span>',
            secondTemplate: '<span data-action="showSeconds"  data-time-component="seconds" class="timepicker-second"></span>'
        };
        S.getTemplate = function() {
            return '<div class="timepicker-picker"><table class="table-condensed"><tr><td><a href="#" class="btn" data-action="incrementHours"><span class="' + i.options.icons.up + '"></span></a></td><td class="separator"></td><td>' + (i.options.useMinutes ? '<a href="#" class="btn" data-action="incrementMinutes"><span class="' + i.options.icons.up + '"></span></a>' : "") + "</td>" + (i.options.useSeconds ? '<td class="separator"></td><td><a href="#" class="btn" data-action="incrementSeconds"><span class="' + i.options.icons.up + '"></span></a></td>' : "") + (i.use24hours ? "" : '<td class="separator"></td>') + "</tr><tr><td>" + S.hourTemplate + '</td> <td class="separator">:</td><td>' + (i.options.useMinutes ? S.minuteTemplate : '<span class="timepicker-minute">00</span>') + "</td> " + (i.options.useSeconds ? '<td class="separator">:</td><td>' + S.secondTemplate + "</td>" : "") + (i.use24hours ? "" : '<td class="separator"></td><td><button type="button" class="btn btn-primary" data-action="togglePeriod"></button></td>') + '</tr><tr><td><a href="#" class="btn" data-action="decrementHours"><span class="' + i.options.icons.down + '"></span></a></td><td class="separator"></td><td>' + (i.options.useMinutes ? '<a href="#" class="btn" data-action="decrementMinutes"><span class="' + i.options.icons.down + '"></span></a>' : "") + "</td>" + (i.options.useSeconds ? '<td class="separator"></td><td><a href="#" class="btn" data-action="decrementSeconds"><span class="' + i.options.icons.down + '"></span></a></td>' : "") + (i.use24hours ? "" : '<td class="separator"></td>') + '</tr></table></div><div class="timepicker-hours" data-action="selectHour"><table class="table-condensed"></table></div><div class="timepicker-minutes" data-action="selectMinute"><table class="table-condensed"></table></div>' + (i.options.useSeconds ? '<div class="timepicker-seconds" data-action="selectSecond"><table class="table-condensed"></table></div>' : "")
        }
            ,
            i.destroy = function() {
                H(),
                    I(),
                    i.widget.remove(),
                    i.element.removeData("DateTimePicker"),
                i.component && i.component.removeData("DateTimePicker")
            }
            ,
            i.show = function(a) {
                if (!l().prop("disabled")) {
                    if (i.options.useCurrent && "" === l().val()) {
                        if (1 !== i.options.minuteStepping) {
                            var c = b()
                                , d = i.options.minuteStepping;
                            c.minutes(Math.round(c.minutes() / d) * d % 60).seconds(0),
                                i.setValue(c.format(i.format))
                        } else
                            i.setValue(b().format(i.format));
                        o("", a.type)
                    }
                    a && "click" === a.type && i.isInput && i.widget.hasClass("picker-open") || (i.widget.hasClass("picker-open") ? (i.widget.hide(),
                        i.widget.removeClass("picker-open")) : (i.widget.show(),
                        i.widget.addClass("picker-open")),
                        i.height = i.component ? i.component.outerHeight() : i.element.outerHeight(),
                        n(),
                        i.element.trigger({
                            type: "dp.show",
                            date: b(i.date)
                        }),
                        G(),
                    a && B(a))
                }
            }
            ,
            i.disable = function() {
                var a = l();
                a.prop("disabled") || (a.prop("disabled", !0),
                    H())
            }
            ,
            i.enable = function() {
                var a = l();
                a.prop("disabled") && (a.prop("disabled", !1),
                    F())
            }
            ,
            i.hide = function() {
                var a, c, d = i.widget.find(".collapse");
                for (a = 0; a < d.length; a++)
                    if (c = d.eq(a).data("collapse"),
                        c && c.transitioning)
                        return;
                i.widget.hide(),
                    i.widget.removeClass("picker-open"),
                    i.viewMode = i.startViewMode,
                    E(),
                    i.element.trigger({
                        type: "dp.hide",
                        date: b(i.date)
                    }),
                    I()
            }
            ,
            i.setValue = function(a) {
                b.locale(i.options.language),
                    a ? i.unset = !1 : (i.unset = !0,
                        K()),
                    a = b.isMoment(a) ? a.locale(i.options.language) : a instanceof Date ? b(a) : b(a, i.format, i.options.useStrict),
                    a.isValid() ? (i.date = a,
                        K(),
                        i.viewDate = b({
                            y: i.date.year(),
                            M: i.date.month()
                        }),
                        t(),
                        x()) : p(a)
            }
            ,
            i.getDate = function() {
                return i.unset ? null : b(i.date)
            }
            ,
            i.setDate = function(a) {
                var c = b(i.date);
                i.setValue(a ? a : null),
                    o(c, "function")
            }
            ,
            i.setDisabledDates = function(a) {
                i.options.disabledDates = O(a),
                i.viewDate && q()
            }
            ,
            i.setEnabledDates = function(a) {
                i.options.enabledDates = O(a),
                i.viewDate && q()
            }
            ,
            i.setMaxDate = function(a) {
                void 0 !== a && (i.options.maxDate = b.isMoment(a) || a instanceof Date ? b(a) : b(a, i.format, i.options.useStrict),
                i.viewDate && q())
            }
            ,
            i.setMinDate = function(a) {
                void 0 !== a && (i.options.minDate = b.isMoment(a) || a instanceof Date ? b(a) : b(a, i.format, i.options.useStrict),
                i.viewDate && q())
            }
            ,
            k()
    };
    a.fn.datetimepicker = function(b) {
        return this.each(function() {
            var c = a(this)
                , e = c.data("DateTimePicker");
            e || c.data("DateTimePicker", new d(this,b))
        })
    }
        ,
        a.fn.datetimepicker.defaults = {
            format: !1,
            pickDate: !0,
            pickTime: !0,
            useMinutes: !0,
            useSeconds: !1,
            useCurrent: !0,
            calendarWeeks: !1,
            minuteStepping: 1,
            minDate: b({
                y: 1900
            }),
            maxDate: b().add(100, "y"),
            showToday: !0,
            collapse: !0,
            language: b.locale(),
            defaultDate: "",
            disabledDates: !1,
            enabledDates: !1,
            icons: {},
            useStrict: !1,
            direction: "auto",
            sideBySide: !1,
            daysOfWeekDisabled: [],
            widgetParent: !1
        }
});
