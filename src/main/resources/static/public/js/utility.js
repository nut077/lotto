function chkInteger() {
  $("input[type='number']").keydown(function (e) {
    const keycode = e.keyCode;
    if (keycode === 69 || keycode === 107 || keycode === 109 || e.key === '-') {
      e.preventDefault();
    }
  });
}