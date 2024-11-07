<template>
    <div class="container">
        <div class="date-selector">
            <div v-for="(day, index) in days" :key="index" class="date-item" :class="{ active: selectedDay === day }" @click="selectDay(day)">
                {{ day.label }}
            </div>
        </div>
        <div class="time-selector">
            <div class="time-slot" v-for="(time, index) in times" :key="index" :class="{ active: selectedTime === time }" @click="selectTime(time)">
                {{ time }}
            </div>
        </div>
        <button class="confirm-button" @click="confirmSelection">确认时间</button>
    </div>
</template>

<script>
export default {
    data() {
        const today = new Date()
        const days = []
        for (let i = 0; i < 3; i++) {
            const date = new Date(today.getTime() + i * 24 * 60 * 60 * 1000)
            const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }
            const label = date.toLocaleDateString('zh-CN', options)
            days.push({ value: date, label })
        }
        return {
            days,
            times: ['08:59', '09:59', '10:59', '11:59', '12:59', '13:59', '14:59', '15:59'],
            selectedDay: days[0].value,
            selectedTime: '09:59'
        }
    },
    methods: {
        selectDay(day) {
            this.selectedDay = day.value
        },
        selectTime(time) {
            this.selectedTime = time
        },
        confirmSelection() {
            const options = { hour: '2-digit', minute: '2-digit' }
            alert(`您选择的时间是：${this.selectedDay.toLocaleDateString('zh-CN', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })} ${this.selectedTime}`)
        }
    }
}
</script>

<style scoped>
.container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
}

.date-selector {
    display: flex;
    justify-content: space-between;
    padding: 10px;
    background-color: #f9f9f9;
    border-bottom: 1px solid #ddd;
}

.date-item {
    cursor: pointer;
    padding: 5px 10px;
    border-radius: 5px;
    transition: background-color 0.3s;
}

.date-item:hover {
    background-color: #e0e0e0;
}

.date-item.active {
    background-color: #007bff;
    color: white;
}

.time-selector {
    display: flex;
    flex-direction: column;
    padding: 10px;
    background-color: #f9f9f9;
    border-bottom: 1px solid #ddd;
}

.time-slot {
    cursor: pointer;
    padding: 5px;
    margin: 5px 0;
    border-radius: 5px;
    transition: background-color 0.3s;
}

.time-slot:hover {
    background-color: #e0e0e0;
}

.time-slot.active {
    background-color: #007bff;
    color: white;
}

.confirm-button {
    display: block;
    width: 100%;
    padding: 10px;
    background-color: #ff5722;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s;
}

.confirm-button:hover {
    background-color: #e64a19;
}

@media (max-width: 768px) {
    .container {
        padding: 10px;
    }
}
</style>
