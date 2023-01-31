export default {
  /* 발신함 -> 재사용 */
  INIT_SEND_DETAIL(state) {
    state.sends.sendDetail = "";
  },
  SET_SEND_DETAIL(state, payload) {
    state.sends.sendDetail = payload;
  },

  /* 발신함 -> 수정 */
  INIT_SEND_UPDATE(state) {
    state.sends.sendUpdate = "";
  },
  SET_SEND_UPDATE(state, payload) {
    state.sends.sendUpdate = payload;
  },
};
