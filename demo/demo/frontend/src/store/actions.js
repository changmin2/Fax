import receives from "./actions/receives";
import sends from "./actions/sends";
import users from "./actions/users";

export default {
  ...users,
  ...receives,
  ...sends,
};
