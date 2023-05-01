export default function Index() {
    return <h1>To store and update the state, we are going to use the useState hook. As both the Header and the Sidebar
        components need to be aware of the state put the shared state into the parent component (DashboardLayout) and
        pass it as a prop to the Header and the Sidebar components.

        Plus, we need to be able to toggle the drawer (open or close) by clicking on the icon in the Header component.
        Thus the state setter (setOpened) should be passed as a prop to the Header component.</h1>
}
